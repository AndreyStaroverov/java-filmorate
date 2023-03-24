package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Autowired UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User addFriends(Long userId, Long friendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException("Пользователя id  не существует, добавтье нового пользователя");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new UserNotFoundException("Пользователя friend не существует, добавтье нового пользователя");
        }
        if (userId < 0 || friendId < 0) {
            throw new UserNotFoundException("Bad id");
        }
        userStorage.getUserById(userId).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(userId);
        return userStorage.getUserById(userId);
    }

    public User deleteFriends(Long userId, Long friendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException("Пользователя id не существует, добавтье нового пользователя");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new UserNotFoundException("Пользователя friend не существует, добавтье нового пользователя");
        }
        if (userId < 0 || friendId < 0) {
            throw new UserNotFoundException("Bad id");
        }
        userStorage.getUserById(userId).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(userId);
        return userStorage.getUserById(userId);
    }

    public Collection<User> getClosedFriends(Long userId, Long friendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException("Пользователя id не существует, добавтье нового пользователя");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new UserNotFoundException("Пользователя other не существует, добавтье нового пользователя");
        }
        if (userId < 0 || friendId < 0) {
            throw new ValidationException("Bad id");
        }
        Set<Long> userFriends = new HashSet<>(userStorage.getUserById(userId).getFriends());
        userFriends.retainAll(userStorage.getUserById(friendId).getFriends());
        List<User> common = new ArrayList<>();
        for (Long user : userFriends) {
            common.add(userStorage.getUserById(user));
        }
        return common;
    }

    public User getUserById(long id) {
        if (userStorage.getUserById(id) == null) {
            throw new UserNotFoundException("Пользователя не существует, добавтье нового пользователя");
        }
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        for (User storedUser : userStorage.findAll()) {
            if (storedUser.getEmail().equals(user.getEmail())) {
                throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            throw new UserNotFoundException("Пользователя не существует, добавтье нового пользователя");
        }
        return userStorage.updateUser(user);
    }

    public Collection<User> getFriendsById(long id) {
        if (userStorage.getUserById(id) == null) {
            throw new UserNotFoundException("Пользователя не существует, добавтье нового пользователя");
        }
        if (id < 0) {
            throw new ValidationException("Bad id");
        }
        return userStorage.getFriendsById(id);
    }
}
