package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    private static final LocalDate LIMIT_DATA = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> findAll() {
        return service.findAll();
    }
    // показать все фильмы

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        return service.create(film);
    }
    // добавить фильм

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validate(film);
        return service.update(film);
    }
    // обнавить фильм

    @GetMapping("/{id}")
    public Film findById(@PathVariable Integer id) {
        return service.findById(id);
    }
    // показать фильм по ID

    @PutMapping("/{id}/like/{userId}")
    public void createLike(@PathVariable Integer id, @PathVariable Integer userId) {
        service.createLike(id, userId);
    }
    // добавить лайк

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        service.deleteLike(id, userId);
    }
    // удалить лайк

    @GetMapping("/popular")
    public List<Film> filmsPopular(@RequestParam(defaultValue = "10", required = false) @Positive Integer count) {
        return service.filmsPopular(count);
    }
    // список из первых фильмов по лайкам

    void validate(Film film) {
        if (film.getReleaseDate().isBefore(LIMIT_DATA))
            throw new ValidationException("Дата релиза не может быть раньше " + LIMIT_DATA);

    }
    // воспомогателльный метод проверяет дату релиза
}
