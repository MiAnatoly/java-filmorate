package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DirectorStorage {

    List<Director> findAll();

    Optional<Director> findById(int id);

    Director create(Director director);

    Director update(Director director);

    void delete(int id);

    List<Director> findByFilmId(int filmId);

    Map<Integer, List<Director>> findByFilmIds(List<Integer> filmIds);
}