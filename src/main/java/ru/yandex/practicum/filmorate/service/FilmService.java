package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.RequestParam;
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

    List<Film> getFilmsByParams(String query, List<String> by);

    List<Film> findFilmsByDirectorSorted(int directorId, String sortType);

    List<Film> getPopularFilmsByGenre(Integer genreId, Integer limit);

    List<Film> getPopularFilmsByYear(Integer year, Integer limit);

    List<Film> getPopularFilmsByGenreAndYear(Integer genreId, Integer year, Integer limit);

    List<Film> getCommonFilms(int userId, int friendId);
}
