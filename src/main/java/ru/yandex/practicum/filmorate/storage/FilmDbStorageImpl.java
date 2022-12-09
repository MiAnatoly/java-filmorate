package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

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
        String sql = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

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
        createFilmCategories(film);
        return film;
    } // добавить фильм

    @Override
    public Film update(Film film) {
        String sqlQuery = "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?" +
                ", DURATION = ?,  RATING_ID = ? where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        deleteFilmCategories(film);
        createFilmCategories(film);
        return film;
    } // обновить фильм

    @Override
    public Optional<Film> findById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", id);
        if (filmRows.next()) {
            return Optional.of(filmRows(filmRows));
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    } // показать филм по id

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

    @Override
    public List<Film> filmsPopular(Integer count) {
        String sql = "SELECT f.film_id, COUNT(lf.user_id) AS count_like " +
                "FROM films AS f LEFT OUTER JOIN like_film AS lf ON f.film_id = lf.film_id " +
                "GROUP BY f.name " +
                "ORDER BY count_like DESC " +
                "LIMIT " + count;
        return jdbcTemplate.query(sql, (rs, rowNum) -> findById(rs.getInt("FILM_ID")).orElse(null));
    } // показать попюлярные филмы

    @Override
    public List<Category> findAllCategory() {
        String sql = "SELECT * FROM CATEGORY";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Category(rs.getInt("CATEGORY_ID"), rs.getString("NAME")));
    } // показать все категории

    @Override
    public Optional<Category> findByIdCategory(Integer id) {
        SqlRowSet categoryRows = jdbcTemplate.queryForRowSet("select * from CATEGORY where CATEGORY_ID = ?", id);
        if (categoryRows.next()) {
            Category category = new Category(categoryRows.getInt("CATEGORY_ID"),
                    categoryRows.getString("NAME"));
            return Optional.of(category);
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    } // показать категорию по id

    @Override
    public List<RatingMpa> findAllMpa() {
        String sql = "SELECT * FROM RATING_MPA";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new RatingMpa(rs.getInt("RATING_ID"), rs.getString("RATING")));
    } // показать список рейтингов

    @Override
    public Optional<RatingMpa> findByIdMpa(Integer id) {
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("select * from RATING_MPA where RATING_ID = ?", id);
        if (ratingRows.next()) {
            RatingMpa ratingMpa = new RatingMpa(ratingRows.getInt("RATING_ID"),
                    ratingRows.getString("RATING"));
            return Optional.of(ratingMpa);
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    } // показать рейтинг по id

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("FILM_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        Integer rate = rs.getInt("RATING_ID");
        return new Film(id, name, description, releaseDate, duration, findByIdMpa(rate).orElse(null), filmCategories(id));
    } // добавить в фильм данные из БД через ResultSet

    private Film filmRows(SqlRowSet filmRows) {
        int id = filmRows.getInt("FILM_ID");
        String name = filmRows.getString("NAME");
        String description = filmRows.getString("DESCRIPTION");
        LocalDate releaseDate = Objects.requireNonNull(filmRows.getDate("RELEASE_DATE")).toLocalDate();
        int duration = filmRows.getInt("DURATION");
        RatingMpa mpa = findByIdMpa(filmRows.getInt("RATING_ID")).orElse(null);
        List<Category> categories = filmCategories(id);
        return new Film(id, name, description, releaseDate, duration, mpa, categories);
    } // добавить в фильм данные из БД через SqlRowSet

    private List<Category> filmCategories(Integer idFilms) {
        String sql = "SELECT CATEGORY_ID FROM FILM_CATEGORY WHERE FILM_ID = " + idFilms +
                " GROUP BY FILM_ID, CATEGORY_ID";
        List<Integer> categories = jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getInt("CATEGORY_ID"));
        List<Category> categoryList = new ArrayList<>();
        for (Integer id : categories) {
            categoryList.add(findByIdCategory(id).orElse(null));
        }
        return categoryList;
    } // вернуть категории фильма
    private void createFilmCategories(Film film) {
        if (film.getGenres() != null) {
            for (Category category : film.getGenres()) {
                String sqlQuery = "insert into FILM_CATEGORY (FILM_ID, CATEGORY_ID) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQuery, film.getId(), category.getId());
            }
        }
    } // добавить категории в БД

    private void deleteFilmCategories(Film film) {
        String sqlQuery = "delete from FILM_CATEGORY where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    } // удалить категории из БД
}
