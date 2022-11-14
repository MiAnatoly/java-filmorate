package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private int idFilms;

    @Override
    public Map<Integer, Film> findAll() {
        return films;
    }
    // показать все фильмы

    @Override
    public Optional<Film> create(Film film) {
        if (!(films.containsValue(film))) {
            idFilms++;
            film.setId(idFilms);
            films.put(film.getId(), film);
        } else
            throw new RuntimeException("Фильм уже есть в базе");
        return Optional.of(film);
    }
    // добавить фильм

    @Override
    public Optional<Film> update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else
            throw new NotObjectException("Фильма нет в базе");
        return Optional.of(film);
    }
    // обнавить фильм

}
