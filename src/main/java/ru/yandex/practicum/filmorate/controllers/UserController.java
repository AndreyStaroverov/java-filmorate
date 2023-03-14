package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping("/users")
    public List<User> findAll() {
        log.debug("Текущее количество users: {}", users.size());
        return List.copyOf(users.values());
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user, BindingResult bindingResult) {
        for (User storedUser : users.values()) {
            if (storedUser.getEmail().equals(user.getEmail())) {
                throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
        }
        user = validateUser(user);
        if (!bindingResult.hasErrors()) {
            user.setId(id++);
            users.put(user.getId(), user);
            log.debug("Добавили id:" + user.getId());
            return user;
        }
        throw new ValidationException("Ошибка валидации при запросе POST, для /users");
    }

    @PutMapping(value = "/users")
    public User put(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (!users.containsKey(user.getId())) {
            throw new UserAlreadyExistException("Пользователя не существует, добавтье нового пользователя");
        }
        user = validateUser(user);
        if (!bindingResult.hasErrors()) {
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.debug("Обновили user id:" + user.getId());
            return user;
        }
        throw new ValidationException("Ошибка валидации при запросе PuT, для /users");
    }

    public User validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user = new User(user.getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
            log.debug("Заменили пустое имя на login");
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("InvalidEmailException in PUT/users");
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
        }
        return user;
    }
}