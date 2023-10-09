package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {

        Throwable thrown = catchThrowable(() -> {
            userStorage.getUserById(1L);
        });
        assertThat(thrown).isInstanceOf(EmptyResultDataAccessException.class);
        assertThat(thrown.getMessage()).isNotBlank();
    }
}
