package ru.yandex.practicum.filmorate.exceptions;

public class ModelOperationException extends RuntimeException {
    public ModelOperationException(String message) {
        super(message);
    }
}
