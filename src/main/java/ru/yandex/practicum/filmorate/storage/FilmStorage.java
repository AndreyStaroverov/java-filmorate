package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long id);

    Collection<Film> getFilms();

    Film addLike(Long filmId, Long userId);

    Film deleteLike(Long filmId, Long userId);
}
