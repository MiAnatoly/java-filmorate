package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@Component
public class UserController {
    private final InMemoryUserStorage storage;
    private final UserService service;

    @Autowired
    public UserController(InMemoryUserStorage storage, UserService service) {
        this.storage = storage;
        this.service = service;
    }

    @GetMapping
    public List<User> findAll() {
        return storage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return storage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return storage.update(user);
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable Integer id) {
        return service.findUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void createFriend(@PathVariable Integer friendId, @PathVariable Integer id) {
        service.createFriend(friendId, id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer friendId, @PathVariable Integer id) {
        service.deleteFriend(friendId, id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable Integer id) {
        return service.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findOtherFriends(@PathVariable Integer otherId, @PathVariable Integer id) {
        return service.findOtherFriends(otherId, id);
    }
}
