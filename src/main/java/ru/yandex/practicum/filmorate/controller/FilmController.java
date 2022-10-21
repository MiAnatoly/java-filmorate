package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    Map<Integer, Film> films = new HashMap<>();
    final private int limitChat = 200;
    final private LocalDate limitData = LocalDate.of(1895, 12, 28);

    int idFilms;

    @GetMapping
    public List<Film> findAll() {
        List<Film> filmsList= new ArrayList<>(films.values());
        log.debug("Количество фильмов в текущий момент: {}", films.size());
        return filmsList;
    }

    @PostMapping
    public Film creat(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            String text = "Добавлен";
            validate(film, text);
        } else
            throw new RuntimeException("Фильм уже есть в базе");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            String text = "Обновлен";
            validate(film, text);
        } else
            throw new RuntimeException("Фильма нет в базе");
        return film;
    }

    void validate(Film film, String text) {
        if (film.getName() == null || film.getName().isBlank())
            throw new ValidationException("Имя не должно быть пустым");
        if (film.getDescription().length() > limitChat)
            throw new ValidationException("Привышен лимит символов " + limitChat);
        if (film.getReleaseDate().isBefore(limitData))
            throw new ValidationException("Дата релиза не может быть раньше " + limitData);
        if (film.getDuration() < 0)
            throw new ValidationException("Продолжительность фильма не может быть меньше 0");
        if (text.equals("Добавлен")) {
            idFilms++;
            film.setId(idFilms);
        }
        films.put(film.getId(), film);
        log.debug("{} фильм: {}", text, film.getName());
    }
}
