package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (filmStorage.getFilmById(filmId) == null) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        if (userId < 1) {
            log.debug("ValidationException in PUT/films");
            throw new UserNotFoundException("userId is bad");
        }
        filmStorage.getFilmById(filmId).getLikes().add(userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        if (userId < 1) {
            log.debug("ValidationException in PUT/films");
            throw new UserNotFoundException("userId is bad");
        }
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
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
        if (filmStorage.getFilmById(id) == null) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        return filmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            log.debug("ValidationException in PUT/films");
            throw new FilmNotFoundException("Not found");
        }
        return filmStorage.updateFilm(film);
    }
}
