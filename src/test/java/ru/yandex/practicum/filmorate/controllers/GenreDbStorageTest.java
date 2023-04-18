package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GenreDbStorageTest {

    @Autowired
    private final GenreDbStorage genreDbStorage;

    @Test
    public void tryGetCollectionWithAllGenres(){

        assertThat(genreDbStorage.findAll()).isNotNull();
        assertThat(genreDbStorage.findAll().size()).isEqualTo(6);
        assertThat(genreDbStorage.findAll().isEmpty()).isFalse();
    }

    @Test
    public void tryGetGenreById(){

        assertThat(genreDbStorage.getGenreById(1L).getName()).isNotBlank();

        assertThat(genreDbStorage.getGenreById(1L).getName()).isEqualTo("Комедия");
        assertThat(genreDbStorage.getGenreById(2L).getName()).isEqualTo("Драма");
        assertThat(genreDbStorage.getGenreById(3L).getName()).isEqualTo("Мультфильм");
        assertThat(genreDbStorage.getGenreById(4L).getName()).isEqualTo("Триллер");
        assertThat(genreDbStorage.getGenreById(5L).getName()).isEqualTo("Документальный");
        assertThat(genreDbStorage.getGenreById(6L).getName()).isEqualTo("Боевик");

    }

    @Test
    public void tryGetIncorrectGenreById(){
        Throwable thrown = catchThrowable(() -> {
            genreDbStorage.getGenreById(500L);
        });
        assertThat(thrown).isInstanceOf(EmptyResultDataAccessException.class);
        assertThat(thrown.getMessage()).isNotBlank();
    }
}