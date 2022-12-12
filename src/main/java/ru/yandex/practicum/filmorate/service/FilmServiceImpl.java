package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.film.CategoryStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeFilmStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final CategoryStorage categoryStorage;
    private final MpaStorage mpaStorage;
    private final LikeFilmStorage likeFilmStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserService userService
            , CategoryStorage categoryStorage, MpaStorage mpaStorage
            , LikeFilmStorage likeFilmStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.categoryStorage = categoryStorage;
        this.mpaStorage = mpaStorage;
        this.likeFilmStorage = likeFilmStorage;
    }

    @Override
    public List<Film> findAll() {
        return categoryStorage.allFilmsCategories(filmStorage.findAll());
    }
    // показать все фильмы

    @Override
    public Film create(Film film) {
       Film film1 = filmStorage.create(film);
       categoryStorage.createFilmCategories(film1);
       film1.setMpa(mpaStorage.findById(film1.getMpa().getId()).orElse(null));
       return categoryStorage.filmCategories(film1);
    }
    // добавить фильм

    @Override
    public Film update(Film film) {
        Film film1 = filmStorage.update(film).orElseThrow(() -> new NotObjectException("нет фильма"));
        categoryStorage.deleteFilmCategories(film1);
        categoryStorage.createFilmCategories(film1);
        film1.setMpa(mpaStorage.findById(film1.getMpa().getId()).orElse(null));
        return categoryStorage.filmCategories(film1);
    }
    // обнавить фильм

    @Override
    public Film findById(Integer id) {
        return categoryStorage.filmCategories(filmStorage.findById(id).orElseThrow(() -> new NotObjectException("нет фильма")));
    }
    // поиск фильма по id

    @Override
    public void createLike(Integer id, Integer userId) {
        userService.findById(userId);
        findById(id);
        likeFilmStorage.createLike(id,userId);
    }
    // пользователь добавляет лайк

    @Override
    public void deleteLike(Integer id, Integer userId) {
        userService.findById(userId);
        findById(id);
        likeFilmStorage.deleteLike(id,userId);
    }
    // пользователь удаляет лайк

    @Override
    public List<Film> filmsPopular(Integer count) {
        return categoryStorage.allFilmsCategories(filmStorage.filmsPopular(count));
    }
    //возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, вернёт первые 10

    @Override
    public List<Category> findAllCategory() {
        return categoryStorage.findAll();
    }
    // Вернуть все категории

    @Override
    public Category findByIdCategory(Integer id) {
        return categoryStorage.findById(id).orElseThrow(() -> new NotObjectException("нет категории"));
    }
    // показать категортю по id

    @Override
    public List<RatingMpa> findAllMpa() {
        return mpaStorage.findAll();
    }
    // вернуть список рейтингов

    @Override
    public RatingMpa findByIdMpa(Integer id) {
        return mpaStorage.findById(id).orElseThrow(() -> new NotObjectException("нет категории"));
    }
    // показать рейтинг по id
}


