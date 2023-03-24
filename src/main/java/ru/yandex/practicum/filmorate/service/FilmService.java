package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {


    private final FilmStorage filmStorage;

    public FilmService(@Autowired FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


    public Film addLike(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId).getLikes().add(userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(Long filmId, Long userId) {
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
        return filmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }
}
