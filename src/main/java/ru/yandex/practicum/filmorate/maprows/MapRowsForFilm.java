package ru.yandex.practicum.filmorate.maprows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class MapRowsForFilm {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MapRowsForFilm(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film mapRowToFilms(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .likes(likesFromDb(resultSet.getLong("film_id")))
                .mpa(mpaFromDb(resultSet.getLong("mpa_id")))
                .genres(genresFromDb(resultSet.getLong("film_id")))
                .build();
    }

    public Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public Set<Long> likesFromDb(Long id) {
        String sqlQuery = "SELECT user_id from likes WHERE film_id  =" + id;
        return new HashSet<>(jdbcTemplate.queryForList(sqlQuery, Long.class));
    }

    public Set<Genre> genresFromDb(Long id) {
        String sqlQuery = "SELECT g.genre_id," +
                " gr.name " +
                "FROM genres AS g " +
                "INNER JOIN genre AS gr ON g.genre_id = gr.genre_id " +
                "WHERE FILM_ID = " + id;
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre));
    }

    public Mpa mpaFromDb(Long id) {
        String sqlQuery = "SELECT mpa_id, name from mpa_rating where mpa_id = " + id;
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa);
    }
}
