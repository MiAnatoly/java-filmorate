package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
@RequiredArgsConstructor
public class FilmController {
    private static final LocalDate LIMIT_DATA = LocalDate.of(1895, 12, 28);
    private final FilmService service;

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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.deleteFilm(id);
    }

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

    @GetMapping("/director/{directorId}")
    public List<Film> filmsByDirectorSortByLikes(@PathVariable int directorId, @RequestParam String sortBy) {
        return service.findFilmsByDirectorSorted(directorId, sortBy);
    }

    @GetMapping(value = "/popular", params = {"genreId"})
    public List<Film> getPopularFilmsByGenre(@RequestParam Integer genreId, @RequestParam(defaultValue = "10") Integer limit) {
        return service.getPopularFilmsByGenre(genreId, limit);
    }

    @GetMapping(value = "/popular", params = {"year"})
    public List<Film> getPopularFilmsByYear(@RequestParam Integer year, @RequestParam(defaultValue = "10") Integer limit) {
        return service.getPopularFilmsByYear(year, limit);
    }

    @GetMapping(value = "/popular", params = {"genreId", "year"})
    public List<Film> getPopularFilmsByGenreAndYear(@RequestParam Integer genreId, @RequestParam Integer year, @RequestParam(defaultValue = "10") Integer limit) {
        return service.getPopularFilmsByGenreAndYear(genreId, year, limit);

    @GetMapping("/search")
    public List<Film> getFilmsByParams(@RequestParam(defaultValue = "", required = false) String query,
                                       @RequestParam(required = false) List<String> by) {
        return service.getFilmsByParams(query, by);
    }

    void validate(Film film) {
        if (film.getReleaseDate().isBefore(LIMIT_DATA))
            throw new ValidationException("Дата релиза не может быть раньше " + LIMIT_DATA);

    }
    // воспомогателльный метод проверяет дату релиза
}
