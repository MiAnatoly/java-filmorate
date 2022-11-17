package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int idUsers;

    @Override
    public Optional<List<User>> findAll() {
        return Optional.of(List.copyOf(users.values()));
    }
    // показать всех пользователей

    @Override
    public User create(User user) {
            idUsers++;
            user.setId(idUsers);
            users.put(user.getId(), user);
        return user;
    }
    // добавить позьзователя

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else
            throw new NotObjectException("Нет пользователя");
        return user;
    }
    // обнавит пользователя

}
