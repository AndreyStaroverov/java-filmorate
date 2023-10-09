package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.maprows.MapRowsForGenres;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> findAll() {
        String sqlQuery = "select genre_id, name from genre";
        return jdbcTemplate.query(sqlQuery, MapRowsForGenres::mapRowToGenre);
    }

    @Override
    public Genre getGenreById(Long id) {
        String sqlQuery = "select genre_id, name from genre where genre_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, MapRowsForGenres::mapRowToGenre, id);
    }
}
