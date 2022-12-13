package ru.yandex.practicum.filmorate.storage.film.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.film.LikeFilmStorage;

@Repository
public class LikeFilmStorageImpl implements LikeFilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeFilmStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createLike(Integer id, Integer userId) {
        String sqlQuery = "INSERT INTO LIKE_FILM (FILM_ID, USER_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    } // добавить лайк

    @Override
    public void deleteLike(Integer id, Integer userId) {
        String sqlQuery = "DELETE FROM LIKE_FILM WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    } // удалить лайк
}
