package com.bookteria.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Result<TValue> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    @JsonProperty("isFailure")
    private final Boolean isFailure;
    private final Error error;
    private final String message;
    private final TValue value;

    protected Result(Boolean isSuccess, String message, TValue value, Error error) {
        if (isSuccess && error != null) {
            throw new IllegalStateException("Success result cannot have an error.");
        }
        if (!isSuccess && error == error.NONE) {
            throw new IllegalStateException("Failure result must have an error.");
        }

        this.isSuccess = isSuccess;
        this.isFailure = !isSuccess;
        this.message = message;
        this.value = value;
        this.error = error;
    }

    public static Result<?> success() {
        return new Result<>(true, null, null, null);
    }

    public static Result<?> success(String message) {
        return new Result<>(true, message, null, null);
    }

    public static <TValue> Result<TValue> success(String message, TValue value) {
        return new Result<>(true, message, value, null);
    }

    public static <TValue> Result<TValue> success(TValue value) {
        return new Result<>(true, null, value, null);
    }

    public static Result<?> Failure(Error error) {
        return new Result<>(false, null, null, error);
    }

    public static Result<?> Failure() {
        return new Result<>(false, null, null, null);
    }
}
