package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

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
        int id = filmStorage.create(film).getId();
        return findById(id);
    }
    // добавить фильм

    @Override
    public Film update(Film film) {
        int id = filmStorage.update(film).getId();
        return findById(id);
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
        findById(id);
        filmStorage.createLike(id,userId);
    }
    // пользователь добавляет лайк

    @Override
    public void deleteLike(Integer id, Integer userId) {
        userService.findById(userId);
        findById(id);
        filmStorage.deleteLike(id,userId);
    }
    // пользователь удаляет лайк

    @Override
    public List<Film> filmsPopular(Integer count) {
        return filmStorage.filmsPopular(count);
    }
    //возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, вернёт первые 10

    @Override
    public List<Category> findAllCategory() {
        return filmStorage.findAllCategory();
    }
    // Вернуть все категории

    @Override
    public Category findByIdCategory(Integer id) {
        return filmStorage.findByIdCategory(id).orElseThrow(() -> new NotObjectException("нет категории"));
    }
    // показать категортю по id

    @Override
    public List<RatingMpa> findAllMpa() {
        return filmStorage.findAllMpa();
    }
    // вернуть список рейтингов

    @Override
    public RatingMpa findByIdMpa(Integer id) {
        return filmStorage.findByIdMpa(id).orElseThrow(() -> new NotObjectException("нет категории"));
    }
    // показать рейтинг по id
}


