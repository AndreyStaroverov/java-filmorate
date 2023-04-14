package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public interface UserStorage {

    Collection<User> findAll();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    Collection<User> getFriendsById(long id);

    User addFriends(Long userId, Long friendId);

    User deleteFriends(Long userId, Long friendId);

    Collection<User> getClosedFriends(Long userId, Long friendId);
}
