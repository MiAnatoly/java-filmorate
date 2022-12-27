package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {

    List<User> findAll();

    User create(User user);

    Optional<User> update(User user);

    void deleteUser(int id);

    Optional<User> findById(Integer id);

    Map<Integer, Map<Integer, Boolean>> findAllUserFilmLike();
}
