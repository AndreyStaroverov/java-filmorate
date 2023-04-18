package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;


@SpringBootTest
@AutoConfigureTestDatabase
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate"})
@SqlGroup({
        @Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {

    @Autowired
    private FilmDbStorage filmDbStorage;

    @Test
    public void test_FindAll() {

        Film film = new Film(1L, "Drama", "Very big",
                LocalDate.of(2020, 12, 20), 90L, new HashSet<>(),
                new Mpa(1L, null), new HashSet<>());

        assertThat(filmDbStorage.findAll().isEmpty()).isTrue();

        filmDbStorage.createFilm(film);

        assertThat(filmDbStorage.findAll().size()).isEqualTo(1);
        assertThat(filmDbStorage.findAll().isEmpty()).isFalse();
    }

    @Test
    public void test_createFilm() {

        Film film = new Film(1L, "Drama", "Very big",
                LocalDate.of(2020, 12, 20), 90L, new HashSet<>(),
                new Mpa(1L, null), new HashSet<>());

        assertThat(filmDbStorage.createFilm(film)).isNotNull();
        assertThat(filmDbStorage.getFilmById(1L).getName()).isEqualTo("Drama");
    }

    @Test
    public void test_updateFilm() {
        Film film = new Film(1L, "Drama", "Very big",
                LocalDate.of(2020, 12, 20), 90L, new HashSet<>(),
                new Mpa(1L, null), new HashSet<>());
        filmDbStorage.createFilm(film);
        Film filmUpdate = new Film(1L, "DramaUpdate", "Very big",
                LocalDate.of(2020, 12, 20), 90L, new HashSet<>(),
                new Mpa(1L, null), new HashSet<>());
        filmDbStorage.updateFilm(filmUpdate);

        assertThat(filmDbStorage.getFilmById(1L).getName()).isEqualTo("DramaUpdate");
        assertThat(filmDbStorage.findAll().size()).isEqualTo(1);
    }

    @Test
    public void test_getFilmById() {

        Film film = new Film(1L, "Drama", "Very big",
                LocalDate.of(2020, 12, 20), 90L, new HashSet<>(),
                new Mpa(1L, null), new HashSet<>());
        filmDbStorage.createFilm(film);

        assertThat(filmDbStorage.getFilmById(1L)).isNotNull();
        assertThat(filmDbStorage.getFilmById(1L).getDescription()).isEqualTo("Very big");

        Throwable thrown = catchThrowable(() -> {
            filmDbStorage.getFilmById(500L);
        });
        assertThat(thrown).isInstanceOf(EmptyResultDataAccessException.class);
        assertThat(thrown.getMessage()).isNotBlank();
    }

    @Test
    public void test_getFilms() {

        Film film = new Film(1L, "Drama", "Very big",
                LocalDate.of(2020, 12, 20), 90L, new HashSet<>(),
                new Mpa(1L, null), new HashSet<>());
        filmDbStorage.createFilm(film);
        Film filmTwo = new Film(1L, "DramaTwo", "Very big",
                LocalDate.of(2020, 12, 20), 90L, new HashSet<>(),
                new Mpa(1L, null), new HashSet<>());
        filmDbStorage.createFilm(filmTwo);

        assertThat(filmDbStorage.getFilms().size()).isEqualTo(2);
        assertThat(filmDbStorage.getFilms().isEmpty()).isFalse();
    }

    @Test
    public void test_addLike() {
        Film film = new Film(1L, "Drama", "Very big",
                LocalDate.of(2020, 12, 20), 90L, new HashSet<>(),
                new Mpa(1L, null), new HashSet<>());
        filmDbStorage.createFilm(film);

        assertThat(filmDbStorage.addLike(1L, 1L).getLikes().size()).isEqualTo(1);
    }

    @Test
    public void test_deleteLike() {
        Film film = new Film(1L, "Drama", "Very big",
                LocalDate.of(2020, 12, 20), 90L, new HashSet<>(),
                new Mpa(1L, null), new HashSet<>());
        filmDbStorage.createFilm(film);

        assertThat(filmDbStorage.addLike(1L, 1L).getLikes().size()).isEqualTo(1);
        assertThat(filmDbStorage.deleteLike(1L, 1L).getLikes().isEmpty()).isTrue();
    }
}