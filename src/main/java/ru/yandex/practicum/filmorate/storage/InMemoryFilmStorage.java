package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private long id = 1;

    @Override
    public Collection<Film> findAll() {
        log.debug("Получение списка фильмов в InMemoryFilmStorage. Текущее количество фильмов: {}", films.size());
        return List.copyOf(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.debug("Фильм создан и добавлен: " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.debug("Фильм обновлен" + film);
        return film;
    }

    public Collection<Film> getFilms() {
        log.debug("Получение List of films");
        return List.copyOf(films.values());
    }

    @Override
    public Film getFilmById(long id) {
        return films.get(id);
    }
}
