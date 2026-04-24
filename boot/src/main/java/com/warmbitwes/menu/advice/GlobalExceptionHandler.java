package com.warmbitwes.menu.advice;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.exception.BizException;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ApiResponse<?> handleBiz(BizException ex) {
        return ApiResponse.failure(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        if (message.isBlank()) {
            message = "参数校验失败";
        }
        return ApiResponse.failure(1001, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<?> handleConstraint(ConstraintViolationException ex) {
        return ApiResponse.failure(1002, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleUnknown(Exception ex) {
        return ApiResponse.failure(500, "系统异常");
    }
}
