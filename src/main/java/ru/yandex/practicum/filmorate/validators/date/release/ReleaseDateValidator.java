package ru.yandex.practicum.filmorate.validators.date.release;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateAnnotation, LocalDate> {

    private static final LocalDate FILM_START_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(ReleaseDateAnnotation constraintAnnotation) {

    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || value.isAfter(FILM_START_DATE);
    }

}
