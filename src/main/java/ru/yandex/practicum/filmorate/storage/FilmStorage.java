package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;
import java.util.Optional;

@Component
public interface FilmStorage {

    Map<Integer, Film> findAll();

    Optional<Film> create(Film film);

    Optional<Film> update(Film film);

}
