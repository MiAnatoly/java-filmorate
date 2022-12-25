package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryStorage {
    List<Category> findAll();

    Optional<Category> findById(Integer id);

    Film filmCategories(Film film); // вернуть категории фильма

    List<Film> allFilmsCategories(List<Film> films); // вернуть категории фильма

    void createFilmCategories(Film film); // добавить категории в БД

    void deleteFilmCategories(int id); // удалить категории из БД

    Map<Integer, List<Category>> filmsCategories(List<Integer> filmIds);
}
