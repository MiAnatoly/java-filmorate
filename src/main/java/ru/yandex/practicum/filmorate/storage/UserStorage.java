package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Optional;

@Component
public interface UserStorage {

    Map<Integer, User> findAll();

    Optional<User> create(User user);

    Optional<User> update(User user);

}
