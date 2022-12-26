package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAll();

    Film create(Film film);

    Optional<Film> update(Film film);

    void delete(int id);

    Optional<Film> findById(Integer id);

    List<Film> filmsPopular(Integer count);

    List<Film> getFilmsByQuery(String query, List<String> byParams);

    List<Film> filmsByDirectorSortByLikes(int directorId);

    List<Film> filmsByDirectorSortByYear(int directorId);

    List<Film> findByIds(List<Integer> ids);

    List<Film> findPopularFilmsByGenre( Integer genreId, Integer limit);

    List<Film> findPopularFilmsByYear( Integer genreId, Integer limit);

    List<Film> findPopularFilmsByGenreAndYear(Integer genreId, Integer year, Integer limit);
}
