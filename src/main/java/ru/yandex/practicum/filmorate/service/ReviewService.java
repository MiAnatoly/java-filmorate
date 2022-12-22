package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {

    Review create(Review review); // Добавление нового отзыва.

    Review update(Review review); // Редактирование уже имеющегося отзыва.

    void delete(Integer id); // Удаление уже имеющегося отзыва.

    Review findById(Integer id); //Получение отзыва по идентификатору.

    List<Review> findAll(Integer filmId, Integer count); // Получение всех отзывов по идентификатору фильма, если фильм не указан, то все. Если кол-во не указано то 10

    void createLike(Integer id, Integer userId); // пользователь ставит лайк отзыву.

    void createDislike(Integer id, Integer userId); // пользователь ставит дизлайк отзыву.

    void deleteLike(Integer id, Integer userId); //  пользователь удаляет лайк отзыву.

    void deleteDislike(Integer id, Integer userId); //  пользователь удаляет лайк отзыву.
}
