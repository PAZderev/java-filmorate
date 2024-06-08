package ru.yandex.practicum.filmorate.utils;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/*
Класс, необходимый для тела ответа посылаемого классом GlobalExceptionHandler
 */
@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String path;
}
