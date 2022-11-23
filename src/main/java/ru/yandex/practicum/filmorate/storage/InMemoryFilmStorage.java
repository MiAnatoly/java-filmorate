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
    public List<Film> findAll() {
        return List.copyOf(films.values());
    }
    // показать все фильмы

    @Override
    public Film create(Film film) {
            idFilms++;
            film.setId(idFilms);
            films.put(film.getId(), film);
        return film;
    }
    // добавить фильм

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else
            throw new NotObjectException("Фильма нет в базе");
        return film;
    }
    // обнавить фильм

    @Override
    public Optional<Film> findById(Integer id) {
        return Optional.ofNullable(films.get(id));
    }
    // поиск фильма по id
}
