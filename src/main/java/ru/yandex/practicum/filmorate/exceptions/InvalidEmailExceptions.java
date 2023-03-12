package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEmailExceptions extends RuntimeException {
    public InvalidEmailExceptions(String s) {
        super(s);
    }
}
