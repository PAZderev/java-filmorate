package ru.yandex.practicum.filmorate.validators.login;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LoginValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAnnotation {
    String message() default "Login must not contain spaces";

    // Обязательные поля для корректной работы кастомной аннотации
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
