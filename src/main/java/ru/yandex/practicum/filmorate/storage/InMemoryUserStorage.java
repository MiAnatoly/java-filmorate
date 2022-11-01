package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int idUsers;

    @Override
    public List<User> findAll() {
        List<User> usersList= new ArrayList<>(users.values());
        log.debug("Количество пользователей в текущий момент: {}", users.size());
        return usersList;
    }

    @Override
    public User create(User user) {
        if (!users.containsKey(user.getId())) {
            String text = "Добавлен";
            validate(user, text);
            idUsers++;
            user.setId(idUsers);
            users.put(user.getId(), user);
        } else
            throw new RuntimeException("Пользователь есть в базе");
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            String text = "Обновлен";
            validate(user, text);
            users.put(user.getId(), user);
        } else
            throw new NotObjectException("Нет пользователя");
        return user;
    }

    void validate(User user, String text) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        log.debug("{} пользователь: {}, email: {}", text, user.getName(), user.getEmail());
    }
}
