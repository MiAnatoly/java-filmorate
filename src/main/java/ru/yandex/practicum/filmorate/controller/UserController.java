package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> findAll() {
        return service.findAll();
    }
    // показать всех пользователей

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        return service.create(user);
    }
    // добавить пользователя

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validate(user);
        return service.update(user);
    }
    // обнавить пользователя

    @GetMapping("/{id}")
    public User findById(@PathVariable Integer id) {
        return service.findById(id);
    }
    // показать пользователя по ID

    @PutMapping("/{id}/friends/{friendId}")
    public void createFriend(@PathVariable Integer friendId, @PathVariable Integer id) {
        service.createFriend(friendId, id);
    }
    // добавить друга

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer friendId, @PathVariable Integer id) {
        service.deleteFriend(friendId, id);
    }
    // удалить друга

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable Integer id) {
        return service.findFriends(id);
    }
    // показать друзей пользователя

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findOtherFriends(@PathVariable Integer otherId, @PathVariable Integer id) {
        return service.findOtherFriends(otherId, id);
    }
    // найти общих друзей с пользователем

    void validate(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());

    }
    // воспомогательный метод проверяет наличие имени и в слючае отсутствия копируется из логина
}
