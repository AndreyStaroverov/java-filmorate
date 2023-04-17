package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@Qualifier("dbStorageFilm")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Film mapRowToFilms(ResultSet resultSet, int rowNum) throws SQLException {
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

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Set<Long> likesFromDb(Long id) {
        String sqlQuery = "SELECT user_id from likes WHERE film_id  =" + id;
        return new HashSet<>(jdbcTemplate.queryForList(sqlQuery, Long.class));
    }

    private Set<Genre> genresFromDb(Long id) {
        String sqlQuery = "SELECT genre_id, " +
                "(SELECT name FROM genre WHERE GENRE_ID = genres.GENRE_ID) AS name " +
                "from genres " +
                "where film_id = " + id;
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre));
    }

    private Mpa mpaFromDb(Long id) {
        String sqlQuery = "SELECT mpa_id, name from mpa_rating where mpa_id = " + id;
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select * from film";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms);
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into film(name, description, release_date , duration , genre_id , mpa_id) " +
                "values (?, ?, ?, ?, ? , ?)";
        if (film.getGenres() == null) {
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getGenres(),
                    film.getMpa().getId());
        } else {
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getGenres().size(),
                    film.getMpa().getId());
            String sqlQueryTwo = "Select film_id from film where name = ?";
            setGenreToDb(film.getGenres(), jdbcTemplate.queryForObject(sqlQueryTwo, Long.class, film.getName()));
        }
        String sqlQueryTwo = "Select film_id, name, description, release_date , duration , genre_id , mpa_id" +
                " from film where name = ?";
        return jdbcTemplate.queryForObject(sqlQueryTwo, this::mapRowToFilms, film.getName());
    }

    private Set<Genre> setGenreToDb(Set<Genre> genres, Long id) {
        String sqlOneQuery = "Delete from genres where film_id = " + id;
        jdbcTemplate.update(sqlOneQuery);
        for (Genre g : genres) {
            String sqlQuery = "insert into genres (film_id, genre_id) " +
                    "values(?,?)";
            jdbcTemplate.update(sqlQuery, id, g.getId());
        }
        return genresFromDb(id);
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
                    null,
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
        return jdbcTemplate.queryForObject(sqlQueryTwo, this::mapRowToFilms, film.getId());
    }

    @Override
    public Film getFilmById(long id) throws EmptyResultDataAccessException {
        String sqlQuery = "select * from film where film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilms, id);
    }

    @Override
    public Collection<Film> getFilms() {
        String sqlQuery = "select * from film";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms);
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
