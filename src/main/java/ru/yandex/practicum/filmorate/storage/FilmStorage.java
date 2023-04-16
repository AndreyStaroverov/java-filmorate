package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<Film> findAll();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long id);

    Collection<Film> getFilms();

    Film addLike(Long filmId, Long userId);

    Film deleteLike(Long filmId, Long userId);
}
