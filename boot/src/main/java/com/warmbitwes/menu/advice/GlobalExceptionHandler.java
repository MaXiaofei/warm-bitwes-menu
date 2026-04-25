package com.warmbitwes.menu.advice;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.exception.BizException;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public ApiResponse<?> handleBiz(BizException ex) {
        log.warn("BizException code={}, message={}", ex.getCode(), ex.getMessage());
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
        log.warn("Validation failed: {}", message);
        return ApiResponse.failure(1001, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<?> handleConstraint(ConstraintViolationException ex) {
        log.warn("Constraint violation: {}", ex.getMessage());
        return ApiResponse.failure(1002, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleUnknown(Exception ex) {
        log.error("Unhandled exception", ex);
        return ApiResponse.failure(500, "系统异常");
    }
}
