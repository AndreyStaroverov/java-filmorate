package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(@Autowired FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> filmsAll() {
        log.debug("Попытка получить список пользователей...");
        return filmService.findAll();
    }

    @GetMapping("/films/{id}")
    public Film filmsAll(@PathVariable("id") Long id) {
        log.debug("Попытка получить фильм по идентификатору");
        if (filmService.getFilmById(id) == null) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        return filmService.getFilmById(id);
    }

    @GetMapping("/films/popular")
    public Collection<Film> filmsMostPopular(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.debug("Попытка получить список пользователей...");
        return filmService.getRateFilms(count);
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.debug("ValidationException in POST/films");
            throw new ValidationException("Ошибка валидации при запросе POST, для /users");
        }
        return filmService.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (filmService.getFilmById(film.getId()) == null) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        if (bindingResult.hasErrors()) {
            log.debug("ValidationException in PUT/films");
            throw new ValidationException("Ошибка валидации при запросе PUT, для /films");
        }
        return filmService.updateFilm(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.debug("Попытка поставить лайк фильму");
        if (filmService.getFilmById(id) == null) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        if (userId < 1) {
            log.debug("ValidationException in PUT/films");
            throw new UserNotFoundException("userId is bad");
        }
        return filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.debug("Попытка удалить лайк у фильма");
        if (filmService.getFilmById(id) == null) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        if (userId < 1) {
            log.debug("ValidationException in PUT/films");
            throw new UserNotFoundException("userId is bad");
        }
        return filmService.deleteLike(id, userId);
    }
}