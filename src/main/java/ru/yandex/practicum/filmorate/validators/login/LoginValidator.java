package ru.yandex.practicum.filmorate.validators.login;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<LoginAnnotation, String> {

    @Override
    public void initialize(LoginAnnotation constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s.indexOf(' ') == -1;
    }
}
