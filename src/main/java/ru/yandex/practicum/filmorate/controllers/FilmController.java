package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @GetMapping("/films")
    public List<Film> filmsAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return List.copyOf(films.values());
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film, BindingResult bindingResult) {

        if (!films.containsKey(film.getId())) {
            throw new FilmAlreadyExistException("Not found");
        }

        if (!bindingResult.hasErrors()) {
            films.put(film.getId(), film);
            log.debug("Фильм обновлен" + film);
            return film;
        }
        log.debug("ValidationException in PUT/films");
        throw new ValidationException("Ошибка валидации при запросе PUT, для /films");

    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            film.setId(id++);
            films.put(film.getId(), film);
            log.debug("Фильм создан и добавлен: " + film);
            return film;
        }
        log.debug("ValidationException in POST/films");
        throw new ValidationException("Ошибка валидации при запросе POST, для /users");

    }
}