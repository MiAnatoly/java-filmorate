package ru.yandex.practicum.filmorate.storage.film.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.film.LikeFilmStorage;

@Repository
public class LikeFilmStorageImpl implements LikeFilmStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeFilmStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createLike(Integer id, Integer userId) {
        String sqlQuery = "insert into LIKE_FILM (FILM_ID, USER_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    } // добавить лайк

    @Override
    public void deleteLike(Integer id, Integer userId) {
        String sqlQuery = "delete from LIKE_FILM where FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    } // удалить лайк
}
