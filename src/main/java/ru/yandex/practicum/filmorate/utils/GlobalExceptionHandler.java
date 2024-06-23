package ru.yandex.practicum.filmorate.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/*
Класс необходимый для логирования ошибок валидации.
Перехватывает исключения, вызываемые валидацией jakarta и логирует их + возвращает в теле ответа.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage());
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errors.append(error.getDefaultMessage()).append("; ");
            log.error("Validation error {}", error.getDefaultMessage());
        });
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(errors.toString())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
