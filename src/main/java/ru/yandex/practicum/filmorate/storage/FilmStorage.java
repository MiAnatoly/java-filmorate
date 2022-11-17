package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Component
public interface FilmStorage {

    Optional<List<Film>>  findAll();

    Film create(Film film);

    Film update(Film film);

}
