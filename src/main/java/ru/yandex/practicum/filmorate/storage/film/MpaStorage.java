package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<RatingMpa> findAll();

    Optional<RatingMpa> findById(Integer id);
}
