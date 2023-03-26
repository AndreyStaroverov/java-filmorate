package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    private long id = 1;

    @Override
    public Collection<User> findAll() {
        log.debug("Получение списка пользователей в InMemoryUserStorage: Текущее количество users: {}", users.size());
        return List.copyOf(users.values());
    }

    @Override
    public User createUser(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        log.debug("Добавили id:" + user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.debug("Обновили user id:" + user.getId());
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public Collection<User> getFriendsById(long id) {
        List<User> userFriends = new ArrayList<>();
        for (Long user : users.get(id).getFriends()) { //Получаем список id друзей
            userFriends.add(users.get(user)); // собираем новый список чтобы вернуть список пользователей, а не id
        }
        return userFriends;
    }
}
