package ru.yandex.practicum.filmorate.storage.film;

public interface LikeFilmStorage {

    void createLike(Integer id, Integer userId); // добавить лайк

    void deleteLike(Integer id, Integer userId);// удалить лайк
}
