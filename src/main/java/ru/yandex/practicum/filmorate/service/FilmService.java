package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

public interface FilmService {
    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    void deleteFilm(int id);

    Film findById(Integer id);

    void createLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    List<Film> filmsPopular(Integer count);

    List<Category> findAllCategory();

    Category findByIdCategory(Integer id);

    List<RatingMpa> findAllMpa();

    RatingMpa findByIdMpa(Integer id);

}
