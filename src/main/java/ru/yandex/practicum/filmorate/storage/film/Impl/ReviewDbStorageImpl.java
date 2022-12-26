package ru.yandex.practicum.filmorate.storage.film.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewDbStorageImpl implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review create(Review review) {
        if (film(review.getFilmId()) && user(review.getUserId())) {
            String sqlQuery = "INSERT INTO REVIEWS (CONTENT, IS_POSITIVE, USER_ID, FILM_ID)" +
                    "values (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"REVIEW_ID"});
                stmt.setString(1, review.getContent());
                stmt.setBoolean(2, review.getIsPositive());
                stmt.setInt(3, review.getUserId());
                stmt.setInt(4, review.getFilmId());
                return stmt;
            }, keyHolder);
            review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());
            return review;
        } else
            throw new NotObjectException("отзыв не добавлен");
    }

    @Override
    public Optional<Review> update(Review review) {
        String sqlQuery = "UPDATE REVIEWS SET CONTENT = ?, IS_POSITIVE = ?" +
                " WHERE REVIEW_ID = ?";
        int result = jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        if (result == 0)
            return Optional.empty();
        return findById(review.getReviewId());

    } // Редактирование уже имеющегося отзыва.

    @Override
    public void delete(Integer id) {
        String sql =
                "DELETE " +
                        "FROM REVIEWS " +
                        "WHERE REVIEW_ID = ?";
        int result = jdbcTemplate.update(sql, id);
        if (result == 1)
            log.info("Удалён отзыв id {}", id);
        else
            throw new NotObjectException("Отзыв не найден для удаления.");
    } // Удаление уже имеющегося отзыва.

    @Override
    public Optional<Review> findById(Integer id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT r.*, (COUNT(LRT.USER_ID) - COUNT(LRF.USER_ID)) AS USE " +
                "FROM REVIEWS AS r " +
                "LEFT JOIN (SELECT * FROM LIKE_REVIEW WHERE IS_POSITIVE = true) LRT on r.REVIEW_ID = LRT.REVIEW_ID " +
                "LEFT JOIN (SELECT * FROM LIKE_REVIEW WHERE IS_POSITIVE = false) LRF on r.REVIEW_ID = LRF.REVIEW_ID " +
                " WHERE r.REVIEW_ID = ? GROUP BY r.REVIEW_ID", id);
        if (reviewRows.next()) {
            return Optional.of(reviewRows(reviewRows));
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    } //Получение отзыва по идентификатору.

    @Override
    public List<Review> findAll(Integer filmId, Integer count) {
        String where = "";
        if (filmId != 0)
            where = "WHERE FILM_ID = " + filmId;

        String sql = "SELECT r.*, (COUNT(LRT.USER_ID) - COUNT(LRF.USER_ID)) AS USE " +
                "FROM REVIEWS AS r " +
                "LEFT JOIN (SELECT * FROM LIKE_REVIEW WHERE IS_POSITIVE = true) LRT on r.REVIEW_ID = LRT.REVIEW_ID " +
                "LEFT JOIN (SELECT * FROM LIKE_REVIEW WHERE IS_POSITIVE = false) LRF on r.REVIEW_ID = LRF.REVIEW_ID " +
                where +
                " GROUP BY r.REVIEW_ID " +
                " ORDER BY COUNT(LRT.USER_ID) - COUNT(LRF.USER_ID) DESC" +
                " LIMIT " + count;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs));
    } // Получение всех отзывов по идентификатору фильма, если фильм не указан, то все. Если кол-во не указано то 10

    private Review makeReview(ResultSet rs) throws SQLException {
        return new Review(rs.getInt("REVIEW_ID"),
                rs.getString("CONTENT"),
                rs.getBoolean("IS_POSITIVE"),
                rs.getInt("USER_ID"),
                rs.getInt("FILM_ID"),
                rs.getInt("USE"));
    } // добавить в отзыв данные из БД через ResultSet

    private Review reviewRows(SqlRowSet rs) {
        return new Review(rs.getInt("REVIEW_ID"),
                rs.getString("CONTENT"),
                rs.getBoolean("IS_POSITIVE"),
                rs.getInt("USER_ID"),
                rs.getInt("FILM_ID"),
                rs.getInt("USE"));
    } // добавить в отзыв данные из БД через SqlRowSet

    private boolean film(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM FILMS " +
                "WHERE FILM_ID = ?", id);
        if (filmRows.next()) {
            return true;
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return false;
    }

    private boolean user(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM USERS_FILMS " +
                "WHERE USER_ID = ?", id);
        if (userRows.next()) {
            return true;
        } else log.info("Пользователь с идентификатором {} не найден.", id);
        return false;
    }
}
