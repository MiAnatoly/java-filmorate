package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
public interface UserStorage {

    List<User> findAll();

    User create(User user);

    User update(User user);

    Optional<User> findById(Integer id);

}
