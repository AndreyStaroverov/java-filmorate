package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public Collection<User> findAll() {
        log.debug("Попытка получить список пользователей...");
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") long id) {
        log.debug("Попытка получить пользователя по идентификатору");
        return userService.getUserById(id);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable("id") long id) {
        log.debug("Попытка получить друзей пользователя");
        return userService.getFriendsById(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonUserFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        log.debug("Попытка получить друзей пользователя");
        return userService.getClosedFriends(id, otherId);
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user, BindingResult bindingResult) {
        user = validateUser(user);
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Ошибка валидации при запросе POST, для /users");
        }
        return userService.createUser(user);
    }

    @PutMapping(value = "/users")
    public User put(@Valid @RequestBody User user, BindingResult bindingResult) {
        user = validateUser(user);
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Ошибка валидации при запросе PuT, для /users");
        }
        return userService.updateUser(user);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public User addFriends(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.debug("Попытка добавить пользователя в друзья");
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public User deleteFriends(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.debug("Попытка удалить из друзей");
        return userService.deleteFriends(id, friendId);
    }

    public User validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Заменили пустое имя на login");
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("InvalidEmailException in PUT/users");
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
        }
        return user;
    }
}