package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmStorage {

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> findById(Integer id);

    void createLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    List<Film> filmsPopular(Integer count);

    List<Category> findAllCategory();

    Optional<Category> findByIdCategory(Integer id);

    List<RatingMpa> findAllMpa();

    Optional<RatingMpa> findByIdMpa(Integer id);
}
