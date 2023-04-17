package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(@Autowired GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre getGenreById(Long id) {
        try {
            genreStorage.getGenreById(id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("ValidationException in PUT/genre");
            throw new GenreNotFoundException("Not found");
        }
        return genreStorage.getGenreById(id);
    }
}
