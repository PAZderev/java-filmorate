package ru.yandex.practicum.filmorate.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.ModelOperationException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

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

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        log.error("NotFoundException: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage() + "; ")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ModelOperationException.class)
    public ResponseEntity<ErrorResponse> handleModelOperationException(RuntimeException e, HttpServletRequest request) {
        log.error("ModelOperationException: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(RuntimeException e, HttpServletRequest request) {
        log.error("InternalServerException: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
