package ru.yandex.practicum.filmorate.validators.date.release;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ReleaseDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReleaseDateAnnotation {
    String message() default "Invalid Release Date";

    // Обязательные поля для корректной работы кастомной аннотации
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
