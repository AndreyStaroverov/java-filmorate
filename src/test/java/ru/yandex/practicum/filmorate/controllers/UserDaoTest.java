package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate"})
@SqlGroup({
        @Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDaoTest {

    @Autowired
    private UserDbStorage userDbStorage;


    @Test
    public void findAlltest(){

        User user = new User(1L, "mailTest@yandex.ru", "login", "Ter",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");

        assertThat(userDbStorage.findAll().isEmpty()).isTrue();

        userDbStorage.createUser(user);

        assertThat(userDbStorage.findAll().size()).isEqualTo(1);
        assertThat(userDbStorage.findAll().isEmpty()).isFalse();

    }
    @Test
    public void createUser(){
        User user = new User(1L, "mailTest@yandex.ru", "login", "Ter",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");

        assertThat(userDbStorage.createUser(user)).isNotNull();
        assertThat(userDbStorage.getUserById(1L).getName()).isEqualTo("Ter");
    }

    @Test
    public void updateUser(){
        User user = new User(1L, "mailTest@yandex.ru", "login", "Ter",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");
        userDbStorage.createUser(user);
        User userUpdate = new User(1L, "mailTest@yandex.ru", "login", "TerUpdate",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");
        userDbStorage.updateUser(userUpdate);

        assertThat(userDbStorage.getUserById(1L).getName()).isEqualTo("TerUpdate");
        assertThat(userDbStorage.findAll().size()).isEqualTo(1);
    }

    @Test
    public void getUserById(){
        User user = new User(1L, "mailTest@yandex.ru", "login", "Ter",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");
        userDbStorage.createUser(user);

        assertThat(userDbStorage.getUserById(1L)).isNotNull();
        assertThat(userDbStorage.getUserById(1L).getEmail()).isEqualTo("mailTest@yandex.ru");
    }

    @Test
    public void getFriendsById(){
        User user = new User(1L, "mailTest@yandex.ru", "login", "Ter",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");
        userDbStorage.createUser(user);
        User userTwo = new User(2L, "mail222Test@yandex.ru", "login2", "Ter2",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");
        userDbStorage.createUser(userTwo);
        userDbStorage.addFriends(user.getId(), userTwo.getId());

        assertThat(userDbStorage.getFriendsById(user.getId()).size()).isEqualTo(1);
        assertThat(userDbStorage.getFriendsById(user.getId())).isNotNull();
    }

    @Test
    public void addFriends(){
        User user = new User(1L, "mailTest@yandex.ru", "login", "Ter",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");
        userDbStorage.createUser(user);
        User userTwo = new User(2L, "mail222Test@yandex.ru", "login2", "Ter2",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");
        userDbStorage.createUser(userTwo);

        assertThat(userDbStorage.addFriends(user.getId(), userTwo.getId())).isNotNull();
        assertThat(userDbStorage.getFriendsById(user.getId()).size()).isEqualTo(1);
    }

    @Test
    public void deleteFriends(){
        User user = new User(1L, "mailTest@yandex.ru", "login", "Ter",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");
        userDbStorage.createUser(user);
        User userTwo = new User(2L, "mail222Test@yandex.ru", "login2", "Ter2",
                LocalDate.of(2002, 10, 5), new HashSet<>(), "confirmed");
        userDbStorage.createUser(userTwo);

        assertThat(userDbStorage.addFriends(user.getId(), userTwo.getId())).isNotNull();
        assertThat(userDbStorage.deleteFriends(user.getId(), userTwo.getId()).getFriends().size()).isEqualTo(0);
    }

}
