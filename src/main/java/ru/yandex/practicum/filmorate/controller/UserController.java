package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    int idUsers;

    @GetMapping
    public List<User> findAll() {
        List<User> usersList= new ArrayList<>(users.values());
        log.debug("Количество пользователей в текущий момент: {}", users.size());
        return usersList;
    }

    @PostMapping
    public User creat(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            String text = "Добавлен";
            validate(user, text);
        } else
            throw new RuntimeException("Пользователь есть в базе");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            String text = "Обновлен";
            validate(user, text);
        } else
            throw new RuntimeException("Нет пользователя");
        return user;
    }

    void validate(User user, String text) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" "))
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения не может быть позже текущей");
        if (text.equals("Добавлен")) {
            idUsers++;
            user.setId(idUsers);
        }
        users.put(user.getId(), user);
        log.debug("{} пользователь: {}, email: {}", text, user.getName(), user.getEmail());

    }
}
