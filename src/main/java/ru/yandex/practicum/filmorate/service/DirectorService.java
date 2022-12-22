package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {

    List<Director> findAll();

    Director findById(int directorId);

    Director create(Director director);

    Director update(Director director);

    void delete(int directorId);
}
