package com.bookteria.identity_services.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /***
     Handle request body is missing or invalid
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 400);
        errorResponse.put("title", "Bad Request");
        errorResponse.put("message", "Request body is missing or invalid");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /***
     Handle runtime exception
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        if (e instanceof AccessDeniedException) {
            throw e;
        }

        int statusCode = getStatusCode(e);

        Map<String, Object> responseBody = Map.of(
                "title", getTitle(e),
                "status", statusCode,
                "message", e.getMessage()
        );

        return ResponseEntity.status(statusCode).body(responseBody);
    }

    /***
     Handle validate exception
     BAD_REQUEST
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        int statusCode = getStatusCode(e);

        // Get all validation fail
        List<Map<String, String>> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> responseBody = Map.of(
                "title", getTitle(e),
                "status", statusCode,
                "errors", errors
        );

        return ResponseEntity.status(statusCode).body(responseBody);
    }

    /***
     Handle access denied
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.FORBIDDEN.value());
        errorResponse.put("title", "Access Denied");
        errorResponse.put("message", "You do not have permission to access this resource");

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    private static final Map<Class<? extends RuntimeException>, Integer> ERROR_STATUS_MAP = Map.of(
            UserException.UserNotFoundException.class, HttpStatus.NOT_FOUND.value(),
            UserException.UserWithUsernameNotFoundException.class, HttpStatus.NOT_FOUND.value(),
            UserException.UserWithUserNameAlreadyExists.class, HttpStatus.BAD_REQUEST.value(),
            PermissionException.PermissionNotFoundException.class, HttpStatus.NOT_FOUND.value(),
            RoleException.RoleNotFoundException.class, HttpStatus.NOT_FOUND.value(),
            IdentityException.TokenEception.class, HttpStatus.UNAUTHORIZED.value(),
            IdentityException.PassWordNotMatchException.class, HttpStatus.BAD_REQUEST.value()
    );

    private static int getStatusCode(RuntimeException exception) {
        return ERROR_STATUS_MAP.getOrDefault(exception.getClass(), HttpStatus.BAD_REQUEST.value());
    }

    private static String getTitle(RuntimeException exception) {
        if (exception instanceof DomainException domainException) {
            return domainException.getTitle();
        }

        return "Server Error";
    }

    private static int getStatusCode(MethodArgumentNotValidException exception) {
        return HttpStatus.BAD_REQUEST.value();
    }

    private static String getTitle(MethodArgumentNotValidException exception) {
        return "Validation Error";
    }
}
