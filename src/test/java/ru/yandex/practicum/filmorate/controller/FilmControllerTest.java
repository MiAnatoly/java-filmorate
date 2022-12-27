package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FilmControllerTest {

    FilmController controller;
    Film film;

    @Test
    public void DataNotEarlier1895Test() {
        controller = new FilmController(null);
        film = new Film(1, "space", "journey into space", LocalDate.of(1895, 12, 27), 220, null, null, Collections.emptyList());
        boolean thrown = false;
        try {
            controller.validate(film);
        } catch (ValidationException e) {
            if (e.getMessage().equals("Дата релиза не может быть раньше " + LocalDate.of(1895, 12, 28)))
                thrown = true;
        }

        assertTrue(thrown);
    }
}
