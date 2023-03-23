package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    UserStorage userStorage;

    @Autowired
    public void setStorage(UserStorage storage) {
        this.userStorage = storage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User addFriends(Long userId, Long friendId) {
        userStorage.getUserById(userId).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(userId);
        return userStorage.getUserById(userId);
    }

    public User deleteFriends(Long userId, Long friendId) {
        userStorage.getUserById(userId).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(userId);
        return userStorage.getUserById(userId);
    }

    public List<User> getClosedFriends(Long userId, Long friendId) {
        Set<Long> userFriends = new HashSet<>(userStorage.getUserById(userId).getFriends());
        userFriends.retainAll(userStorage.getUserById(friendId).getFriends());
        List<User> common = new ArrayList<>();
        for (Long user : userFriends) {
            common.add(userStorage.getUserById(user));
        }
        return common;
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getFriendsById(long id) {
        return userStorage.getFriendsById(id);
    }
}
