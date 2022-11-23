package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    private final UserService userService;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    @Override
    public List<Film> findAll() {
        return filmStorage.findAll();
    }
    // показать все фильмы

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }
    // добавить фильм

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }
    // обнавить фильм

    @Override
    public Film findById(Integer id) {
        return filmStorage.findById(id).orElseThrow(() -> new NotObjectException("нет фильма"));
    }
    // поиск фильма по id

    @Override
    public void createLike(Integer id, Integer userId) {
        userService.findById(userId);
        Film film = findById(id);
        film.getLikeUserId().add(userId);
    }
    // пользователь добавляет лайк

    @Override
    public void deleteLike(Integer id, Integer userId) {
        userService.findById(userId);
        Film film = findById(id);
        film.getLikeUserId().remove(userId);
    }
    // пользователь удаляет лайк

    @Override
    public List<Film> filmsPopular(Integer count) {
        return findAll().stream()
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

}


