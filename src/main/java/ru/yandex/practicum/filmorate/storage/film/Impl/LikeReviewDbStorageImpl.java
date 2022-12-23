package ru.yandex.practicum.filmorate.storage.film.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.film.LikeReviewStorage;

@Repository
public class LikeReviewDbStorageImpl implements LikeReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeReviewDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createLike(Integer id, Integer userId) {
        String sqlQuery = "INSERT INTO LIKE_REVIEW (REVIEW_ID, USER_ID, IS_POSITIVE) " +
                "VALUES (?, ?, TRUE)";
        jdbcTemplate.update(sqlQuery, id, userId);
    } // пользователь ставит лайк отзыву.

    @Override
    public void createDislike(Integer id, Integer userId) {
        String sqlQuery = "INSERT INTO LIKE_REVIEW (REVIEW_ID, USER_ID, IS_POSITIVE) " +
                "VALUES (?, ?, FALSE)";
        jdbcTemplate.update(sqlQuery, id, userId);
    } // пользователь ставит дизлайк отзыву.

    @Override
    public void deleteLike(Integer id, Integer userId) {
        String sqlQuery = "DELETE FROM LIKE_REVIEW WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_POSITIVE = TRUE";
        jdbcTemplate.update(sqlQuery, id, userId);
    } //  пользователь удаляет лайк отзыву.

    @Override
    public void deleteDislike(Integer id, Integer userId) {
        String sqlQuery = "DELETE FROM LIKE_REVIEW WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_POSITIVE = FALSE";
        jdbcTemplate.update(sqlQuery, id, userId);
    } //  пользователь удаляет лайк отзыву.
}
