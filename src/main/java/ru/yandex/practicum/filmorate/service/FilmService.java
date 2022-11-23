package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film findById(Integer id);

    void createLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    List<Film> filmsPopular(Integer count);
}
