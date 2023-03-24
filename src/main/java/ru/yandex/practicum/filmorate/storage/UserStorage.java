package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Component
public interface UserStorage {

    Collection<User> findAll();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    Collection<User> getFriendsById(long id);
}
