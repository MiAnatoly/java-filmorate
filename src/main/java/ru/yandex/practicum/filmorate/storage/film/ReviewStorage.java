package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Review create(Review review); // Добавление нового отзыва.

    Optional<Review> update(Review review); // Редактирование уже имеющегося отзыва.

    void delete(Integer id); // Удаление уже имеющегося отзыва.

    Optional<Review> findById(Integer id); //Получение отзыва по идентификатору.

    List<Review> findAll(Integer filmId, Integer count); // Получение всех отзывов по идентификатору фильма, если фильм не указан, то все. Если кол-во не указано то 10

}
