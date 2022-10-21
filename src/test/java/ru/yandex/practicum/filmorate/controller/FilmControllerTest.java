package ru.yandex.practicum.filmorate.controller;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FilmControllerTest {

    FilmController controller;
    Film film;
    String creat = "Добавлен";
    String update = "Обновлен";

    @Test
    public void CreatTest() {
        controller = new FilmController();
        film = new Film(1, "space", "journey into space", LocalDate.of(1898, 12, 29), 100);
        controller.validate(film, creat);
        assertEquals(1, controller.films.size(), "Не совпадает колтчество фильмов");
    }

    @Test
    public void BlankNameTest() {
        controller = new FilmController();
        film = new Film(1, null, "journey into space", LocalDate.of(2002, 1, 12), 220);
        boolean thrown = false;
        try {
            controller.validate(film, update);
        } catch (ValidationException e) {
            if (e.getMessage().equals("Имя не должно быть пустым"))
                thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void limitChat200DescriptionTest() {
        controller = new FilmController();
        film = new Film(1, "space", RandomString.make(201), LocalDate.of(2002, 1, 12), 220);
        boolean thrown = false;
        try {
            controller.validate(film, update);
        } catch (ValidationException e) {
            if (e.getMessage().equals("Привышен лимит символов 200"))
                thrown = true;
        }

        assertTrue(thrown);
    }

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

    @Test
    public void DurationNotNegativeTest() {
        controller = new FilmController();
        film = new Film(1, "space", "journey into space", LocalDate.of(1898, 12, 29), -1);
        boolean thrown = false;
        try {
            controller.validate(film, update);
        } catch (ValidationException e) {
            if (e.getMessage().equals("Продолжительность фильма не может быть меньше 0"))
                thrown = true;
        }

        assertTrue(thrown);
    }
}
