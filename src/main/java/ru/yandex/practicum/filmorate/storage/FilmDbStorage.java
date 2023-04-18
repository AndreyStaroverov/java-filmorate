package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.maprows.MapRowsForFilm;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

@Component
@Qualifier("dbStorageFilm")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MapRowsForFilm mapRowsForFilm;

    public FilmDbStorage(@Autowired JdbcTemplate jdbcTemplate, MapRowsForFilm mapRowsForFilm) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapRowsForFilm = mapRowsForFilm;
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select * from film";
        return jdbcTemplate.query(sqlQuery, mapRowsForFilm::mapRowToFilms);
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into film(name, description, release_date , duration , likes_id, genre_id, mpa_id) " +
                "values (?, ?, ?, ?, ? , ?, ?)";

        if (film.getGenres() == null) {
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    0, 0,
                    film.getMpa().getId());
        } else {
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    0,
                    film.getGenres().size(),
                    film.getMpa().getId());
            String sqlQueryTwo = "Select film_id from film where name = ?";
            setGenreToDb(film.getGenres(), jdbcTemplate.queryForObject(sqlQueryTwo, Long.class, film.getName()));
        }
        String sqlQueryTwo = "Select film_id, name, description, release_date , duration , genre_id , mpa_id" +
                " from film where name = ?";
        return jdbcTemplate.queryForObject(sqlQueryTwo, mapRowsForFilm::mapRowToFilms, film.getName());
    }

    private Set<Genre> setGenreToDb(Set<Genre> genres, Long id) {
        String sqlOneQuery = "Delete from genres where film_id = " + id;
        jdbcTemplate.update(sqlOneQuery);
        for (Genre g : genres) {
            String sqlQuery = "insert into genres (film_id, genre_id) " +
                    "values(?,?)";
            jdbcTemplate.update(sqlQuery, id, g.getId());
        }
        return mapRowsForFilm.genresFromDb(id);
    }

    @Override
    public Film updateFilm(Film film) throws EmptyResultDataAccessException {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            String sqlQuery = "update film set " +
                    "name = ?, description = ?, release_date = ? , duration = ? , genre_id = ?, mpa_id = ? " +
                    "where film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    0,
                    film.getMpa().getId(),
                    film.getId());
            String sqlOneQuery = "Delete from genres where film_id = " + film.getId();
            jdbcTemplate.update(sqlOneQuery);
        } else {
            String sqlQuery = "update film set " +
                    "name = ?, description = ?, release_date = ? , duration = ? , genre_id = ?, mpa_id = ? " +
                    "where film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    setGenreToDb(film.getGenres(), film.getId()).size(),
                    film.getMpa().getId(),
                    film.getId());
        }
        String sqlQueryTwo = "Select * from film where film_id = ?";
        return jdbcTemplate.queryForObject(sqlQueryTwo, mapRowsForFilm::mapRowToFilms, film.getId());
    }

    @Override
    public Film getFilmById(long id) throws EmptyResultDataAccessException {
        String sqlQuery = "select * from film where film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, mapRowsForFilm::mapRowToFilms, id);
    }

    @Override
    public Collection<Film> getFilms() {
        String sqlQuery = "select * from film";
        return jdbcTemplate.query(sqlQuery, mapRowsForFilm::mapRowToFilms);
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        String sqlQuery = "insert into likes (film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return getFilmById(filmId);
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        String sqlQuery = "delete from likes where user_id = ? AND film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        return getFilmById(filmId);
    }
}
