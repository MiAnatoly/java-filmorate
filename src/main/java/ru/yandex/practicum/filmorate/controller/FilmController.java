package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Component
public class FilmController {
    private final InMemoryFilmStorage storage;
    private final FilmService service;

    @Autowired
    public FilmController(InMemoryFilmStorage storage, FilmService service) {
        this.storage = storage;
        this.service = service;
    }

    @GetMapping
    public List<Film> findAll() {
        return storage.findAll();
    }
    // показать все фильмы

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return storage.create(film);
    }
    // добавить фильм

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return storage.update(film);
    }
    // обнавить фильм

    @GetMapping("/{id}")
    public Film findUser(@PathVariable Integer id) {
        return service.findFilm(id);
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
    public List<Film> filmsPopular(@RequestParam (defaultValue = "10", required = false) Integer count) {
        return service.filmsPopular(count);
    }
    // список из первых фильмов по лайкам


}
