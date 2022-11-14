package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    protected final Map<Integer, User> users = new HashMap<>();
    protected int idUsers;

    @Override
    public Map<Integer, User> findAll() {
        return users;
    }
    // показать всех пользователей

    @Override
    public Optional<User> create(User user) {
        if (!users.containsKey(user.getId())) {
            idUsers++;
            user.setId(idUsers);
            users.put(user.getId(), user);
        } else
            throw new RuntimeException("Пользователь есть в базе");
        return Optional.of(user);
    }
    // добавить позьзователя

    @Override
    public Optional<User> update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else
            throw new NotObjectException("Нет пользователя");
        return Optional.of(user);
    }
    // обнавит пользователя

}
