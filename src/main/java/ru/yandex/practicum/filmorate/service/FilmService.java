package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film findFilm(Integer id) {
        if(!filmStorage.getFilms().containsKey(id))
            throw new NotObjectException("нет данного пользователя");
        return filmStorage.getFilms().get(id);
    }

    public void createLike(Integer id, Integer userId) {
        if (!filmStorage.getFilms().containsKey(id))
            throw new NotObjectException("нет данного фильма");
        if (!userStorage.getUsers().containsKey(userId))
            throw new NotObjectException("нет данного пользователя");
        filmStorage.getFilms().get(id).getLikeUserId().add(userId);

    }

    public void deleteLike(Integer id, Integer userId) {
        if (!filmStorage.getFilms().containsKey(id))
            throw new NotObjectException("нет данного фильма");
        if (!userStorage.getUsers().containsKey(userId))
            throw new NotObjectException("нет данного пользователя");
        filmStorage.getFilms().get(id).getLikeUserId().remove(userId);
    }

    public List<Film> filmsPopular(Integer count) {
        if (count <= 0)
            throw new RuntimeException("Число не может быть меньше или равно 0");
        return new ArrayList<>(filmStorage.getFilms().values()).stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return f1.getLikeUserId().size() - f0.getLikeUserId().size();
    }
}


