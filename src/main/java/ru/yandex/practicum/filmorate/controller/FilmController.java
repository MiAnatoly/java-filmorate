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
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate LIMIT_DATA = LocalDate.of(1895, 12, 28);
    private int idFilms;

    @GetMapping
    public List<Film> findAll() {
        List<Film> filmsList= new ArrayList<>(films.values());
        log.debug("Количество фильмов в текущий момент: {}", films.size());
        return filmsList;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            String text = "Добавлен";
            validate(film, text);
            idFilms++;
            film.setId(idFilms);
            films.put(film.getId(), film);
        } else
            throw new RuntimeException("Фильм уже есть в базе");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            String text = "Обновлен";
            validate(film, text);
            films.put(film.getId(), film);
        } else
            throw new RuntimeException("Фильма нет в базе");
        return film;
    }

    void validate(Film film, String text) {
        if (film.getReleaseDate().isBefore(LIMIT_DATA))
            throw new ValidationException("Дата релиза не может быть раньше " + LIMIT_DATA);
        log.debug("{} фильм: {}", text, film.getName());
    }
}
