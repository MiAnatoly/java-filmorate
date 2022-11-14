package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService implements FilmServiceInterface {

    private final FilmStorage filmStorage;

    private final UserServiceInterface userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserServiceInterface userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(filmStorage.findAll().values());
    }
    // показать все фильмы

    @Override
    public Film create(Film film) {
        return filmStorage.create(film).orElseThrow(() -> new NotObjectException("объект отсутствует"));
    }
    // добавить фильм

    @Override
    public Film update(Film film) {
        return filmStorage.update(film).orElseThrow(() -> new NotObjectException("объект отсутствует"));
    }
    // обнавить фильм

    @Override
    public Film findFilm(Integer id) {
        return findById(id, new NotObjectException("нет данного пользователя"));
    }

    @Override
    public void createLike(Integer id, Integer userId) {
        findById(id, new NotObjectException("нет данного фильма"));
        userService.findUser(userId);
        filmStorage.findAll().get(id).getLikeUserId().add(userId);
    }
    // пользователь добавляет лайк

    @Override
    public void deleteLike(Integer id, Integer userId) {
        findById(id, new NotObjectException("нет данного фильма"));
        userService.findUser(userId);
        filmStorage.findAll().get(id).getLikeUserId().remove(userId);
    }
    // пользователь удаляет лайк

    @Override
    public List<Film> filmsPopular(Integer count) {
        if (count <= 0)
            throw new RuntimeException("Число не может быть меньше или равно 0");
        return new ArrayList<>(filmStorage.findAll().values()).stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }
    //возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, вернёт первые 10

    private int compare(Film f0, Film f1) {
        return f1.getLikeUserId().size() - f0.getLikeUserId().size();
    }
    // определяет по количеству лайков

    private Film findById(Integer id, RuntimeException exception) {
        return Optional.ofNullable(filmStorage.findAll().get(id)).orElseThrow(() -> exception);
    }
    //поиск фильма по id
}


