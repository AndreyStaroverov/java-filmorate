package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FilmAlreadyExistException extends RuntimeException {
    public FilmAlreadyExistException(String s) {
        super(s);
    }
}
