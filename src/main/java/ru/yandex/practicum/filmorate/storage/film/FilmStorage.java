package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Search;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAll();

    Film create(Film film);

    Optional<Film> update(Film film);

    void deleteFilm(int id);

    Optional<Film> findById(Integer id);

    List<Film> filmsPopular(Integer count);

    List<Film> searchForFilm(Search search);
}
