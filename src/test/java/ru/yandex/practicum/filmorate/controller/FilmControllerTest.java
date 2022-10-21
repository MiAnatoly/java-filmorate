package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FilmControllerTest {

    FilmController controller;
    Film film;
    String update = "Обновлен";

    @Test
    public void DataNotEarlier1895Test() {
        controller = new FilmController();
        film = new Film(1, "space", "journey into space", LocalDate.of(1895, 12, 27), 220);
        boolean thrown = false;
        try {
            controller.validate(film, update);
        } catch (ValidationException e) {
            if (e.getMessage().equals("Дата релиза не может быть раньше " + LocalDate.of(1895, 12, 28)))
                thrown = true;
        }

        assertTrue(thrown);
    }
}
