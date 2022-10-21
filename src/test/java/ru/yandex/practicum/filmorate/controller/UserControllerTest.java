package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {
    UserController controller;
    User user;
    String update = "Обновлен";

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
