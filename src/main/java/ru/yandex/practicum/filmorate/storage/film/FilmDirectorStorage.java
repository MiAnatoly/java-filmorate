package ru.yandex.practicum.filmorate.storage.film;

import java.util.List;

public interface FilmDirectorStorage {

    void create(int filmId, int directorId);

    void createBatch(int filmId, List<Integer> directorIds);

    void delete(int filmId);

}
