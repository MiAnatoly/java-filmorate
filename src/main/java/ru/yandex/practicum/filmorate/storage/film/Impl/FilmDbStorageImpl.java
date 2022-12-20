package ru.yandex.practicum.filmorate.storage.film.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Repository
public class FilmDbStorageImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM FILMS LEFT JOIN RATING_MPA RM on FILMS.RATING_ID = RM.RATING_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    } // показать фильмы

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    } // добавить фильм

    @Override
    public Optional<Film> update(Film film) {
        String sqlQuery = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?" +
                ", DURATION = ?,  RATING_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT FILM_ID FROM FILMS " +
                "WHERE FILM_ID = ? ", film.getId());
        if (filmRows.next()) {
            return Optional.of(film);
        } else log.info("Фильм с идентификатором {} не найден.", film.getId());
        return Optional.empty();

    } // обновить фильм

    @Override
    public void deleteFilm(int id) {
        String sql =
                "DELETE " +
                        "FROM FILMS " +
                        "WHERE FILM_ID = ?";
        int result = jdbcTemplate.update(sql, id);
        if(result == 1)
            log.info("Удалён фильм id {}", id);
        else
            throw new NotObjectException("Фильм не найден для удаления.");
    } // удалить фильм

    @Override
    public Optional<Film> findById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS" +
                " LEFT JOIN RATING_MPA RM on FILMS.RATING_ID = RM.RATING_ID WHERE FILM_ID = ? ", id);
        if (filmRows.next()) {
            return Optional.of(filmRows(filmRows));
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    } // показать филм по id

    @Override
    public List<Film> filmsPopular(Integer count) {
        String sql = "SELECT f.*, rm.RATING " +
                "FROM FILMS AS f LEFT OUTER JOIN like_film AS lf ON f.FILM_ID = lf.FILM_ID " +
                "LEFT JOIN RATING_MPA rm on f.RATING_ID = rm.RATING_ID " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY COUNT(lf.USER_ID) DESC " +
                "LIMIT " + count;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    } // показать попюлярные филмы

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("FILM_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        Integer ratingId = rs.getInt("RATING_ID");
        String rate = rs.getString("RATING");
        return new Film(id, name, description, releaseDate, duration, new RatingMpa(ratingId, rate), new ArrayList<>());
    } // добавить в фильм данные из БД через ResultSet

    private Film filmRows(SqlRowSet filmRows) {
        int id = filmRows.getInt("FILM_ID");
        String name = filmRows.getString("NAME");
        String description = filmRows.getString("DESCRIPTION");
        LocalDate releaseDate = Objects.requireNonNull(filmRows.getDate("RELEASE_DATE")).toLocalDate();
        int duration = filmRows.getInt("DURATION");
        RatingMpa mpa = new RatingMpa(filmRows.getInt("RATING_ID"), filmRows.getString("RATING"));
        return new Film(id, name, description, releaseDate, duration, mpa, new ArrayList<>());
    } // добавить в фильм данные из БД через SqlRowSet

}
