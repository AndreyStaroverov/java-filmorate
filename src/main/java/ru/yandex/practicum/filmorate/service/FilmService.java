package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {


    private final FilmStorage filmStorage;

    public FilmService(@Autowired FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


    public Film addLike(Long filmId, Long userId) {
        try {
            filmStorage.getFilmById(filmId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        if (userId < 1) {
            log.debug("ValidationException in PUT/films");
            throw new UserNotFoundException("userId is bad");
        }
        filmStorage.addLike(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(Long filmId, Long userId) {
        try {
            filmStorage.getFilmById(filmId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        if (userId < 1) {
            log.debug("ValidationException in PUT/films");
            throw new UserNotFoundException("userId is bad");
        }
        filmStorage.deleteLike(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public Collection<Film> getRateFilms(int count) {
        if (filmStorage.getFilms().isEmpty()) {
            return List.copyOf(filmStorage.getFilms());
        }
        return List.copyOf(filmStorage.getFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList()));
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilmById(Long id) {
        try {
            filmStorage.getFilmById(id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        return filmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        try {
            filmStorage.getFilmById(film.getId());
        } catch (EmptyResultDataAccessException e) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        return filmStorage.updateFilm(film);
    }
}
