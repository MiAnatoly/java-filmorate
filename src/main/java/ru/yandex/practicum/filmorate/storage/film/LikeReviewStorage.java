package ru.yandex.practicum.filmorate.storage.film;

public interface LikeReviewStorage {

    void createLike(Integer id, Integer userId); // пользователь ставит лайк отзыву.

    void createDislike(Integer id, Integer userId); // пользователь ставит дизлайк отзыву.

    void deleteLike(Integer id, Integer userId); //  пользователь удаляет лайк отзыву.

    void deleteDislike(Integer id, Integer userId); //  пользователь удаляет лайк отзыву.
}
