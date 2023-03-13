package ru.yandex.practicum.filmorate.vakidations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmDateApproveValidator implements ConstraintValidator<FilmDateApprove, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date.isAfter(LocalDate.of(1895, 12, 28));
    }

}
