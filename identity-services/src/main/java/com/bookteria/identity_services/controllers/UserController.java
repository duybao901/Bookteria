package com.bookteria.identity_services.controllers;

import com.bookteria.shared.Result;
import com.bookteria.identity_services.dto.request.UserCreationRequest;
import com.bookteria.identity_services.dto.request.UserUpdateRequest;
import com.bookteria.identity_services.dto.response.UserResponse;
import com.bookteria.identity_services.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService _userService;

    @Autowired
    public UserController(UserService _userService) {
        this._userService = _userService;
    }

    @PostMapping
    public Result<UserResponse> CreateUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse userResponse = _userService.createUser(request);
        return Result.success(userResponse);
    }

    @GetMapping
    public Result<List<UserResponse>> getUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("User: " + authentication.getName());
            System.out.println("Roles: " + authentication.getAuthorities());
        } else {
            System.out.println("No authenticated user found.");
        }

        return Result.success(_userService.getUsers());
    }

    @GetMapping("/{userId}")
    public Result<UserResponse> getUser(@PathVariable String userId) {
        return Result.success(_userService.getUserById(userId));
    }

    @PutMapping("/{userId}")
    public Result<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest user) {
        return Result.success(_userService.updateUser(userId, user));
    }

    @DeleteMapping("/{userId}")
    public Result<UserResponse> deleteUser(@PathVariable String userId) {
        return Result.success(_userService.deleteUser(userId));
    }

    @GetMapping("/myInfo")
    public Result<UserResponse> getMyInfo() {
        return Result.success(_userService.getMyInfo());
    }
}
