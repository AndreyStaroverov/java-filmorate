package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MpaDaoTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    public void tryGetCollectionWithAllMpas(){

        assertThat(mpaDbStorage.findAll()).isNotNull();
        assertThat(mpaDbStorage.findAll().size()).isEqualTo(5);
        assertThat(mpaDbStorage.findAll().isEmpty()).isFalse();
    }

    @Test
    public void tryGetMpaById(){

        assertThat(mpaDbStorage.getMpaById(1L).getName()).isNotBlank();

        assertThat(mpaDbStorage.getMpaById(1L).getName()).isEqualTo("G");
        assertThat(mpaDbStorage.getMpaById(2L).getName()).isEqualTo("PG");
        assertThat(mpaDbStorage.getMpaById(3L).getName()).isEqualTo("PG-13");
        assertThat(mpaDbStorage.getMpaById(4L).getName()).isEqualTo("R");
        assertThat(mpaDbStorage.getMpaById(5L).getName()).isEqualTo("NC-17");

    }

    @Test
    public void tryGetIncorrectMpaById(){
        Throwable thrown = catchThrowable(() -> {
            mpaDbStorage.getMpaById(500L);
        });
        assertThat(thrown).isInstanceOf(EmptyResultDataAccessException.class);
        assertThat(thrown.getMessage()).isNotBlank();
    }

}