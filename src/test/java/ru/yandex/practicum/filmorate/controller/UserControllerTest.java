package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {
    UserController controller;
    User user;
    String creat = "Добавлен";
    String update = "Обновлен";

    @Test
    public void CreatTest() {
        controller = new UserController();
        user = new User(1, "space@mail.ru", "space", "Соня", LocalDate.of(1998, 12, 29));
        controller.validate(user, creat);
        assertEquals(1, controller.users.size(), "Не совпадает колтчество фильмов");
    }

    @Test
    public void BlankAndWhitespaceLoginTest() {
        controller = new UserController();
        user = new User(1, "space@mail.ru", null, "Соня", LocalDate.of(1998, 12, 29));
        User user2 = new User(1, "space@mail.ru", "spa ce", "Соня", LocalDate.of(1998, 12, 29));
        boolean thrown = false;
        try {
            controller.validate(user, update);
        } catch (ValidationException e) {
            if (e.getMessage().equals("Логин не может быть пустым или содержать пробелы"))
                thrown = true;
        }
        boolean thrown2 = false;
        try {
            controller.validate(user2, update);
        } catch (ValidationException e) {
            if (e.getMessage().equals("Логин не может быть пустым или содержать пробелы"))
                thrown2 = true;
        }
        assertTrue(thrown);
        assertTrue(thrown2);
    }

    @Test
    public void NowBirthdayTest() {
        controller = new UserController();
        user = new User(1, "space@mail.ru", "space", "Соня", LocalDate.now().plusDays(1));
        boolean thrown = false;
        try {
            controller.validate(user, update);
        } catch (ValidationException e) {
            if (e.getMessage().equals("Дата рождения не может быть позже текущей"))
                thrown = true;
        }
        assertTrue(thrown);

    }
}
