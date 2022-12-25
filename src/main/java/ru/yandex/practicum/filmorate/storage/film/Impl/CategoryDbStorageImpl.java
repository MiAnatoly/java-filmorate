package ru.yandex.practicum.filmorate.storage.film.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.CategoryStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class CategoryDbStorageImpl implements CategoryStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT * FROM CATEGORY";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Category(rs.getInt("CATEGORY_ID"), rs.getString("CATEGORY")));
    } // показать все категории

    @Override
    public Optional<Category> findById(Integer id) {
        SqlRowSet categoryRows = jdbcTemplate.queryForRowSet("SELECT * FROM CATEGORY WHERE CATEGORY_ID = ?", id);
        if (categoryRows.next()) {
            Category category = new Category(categoryRows.getInt("CATEGORY_ID"),
                    categoryRows.getString("CATEGORY"));
            return Optional.of(category);
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    } // показать категорию по id

    @Override
    public Film filmCategories(Film film) {
        String sql = "SELECT * FROM FILM_CATEGORY AS f " +
                "LEFT OUTER JOIN CATEGORY AS C ON f.CATEGORY_ID = c.CATEGORY_ID " +
                "WHERE FILM_ID = " + film.getId() +
                " GROUP BY FILM_ID, f.CATEGORY_ID" +
                " ORDER BY f.CATEGORY_ID";
        film.setGenres(jdbcTemplate.query(sql,
                (rs, rowNum) -> new Category(rs.getInt("CATEGORY_ID"), rs.getString("CATEGORY"))));
        return film;
    } // вернуть категории фильма

    @Override
    public List<Film> allFilmsCategories(List<Film> films) {
        if (films.isEmpty()) {
            return films;
        }
        Map<Integer, Film> filmsMap = films.stream().collect(Collectors.toMap(
                Film::getId, Function.identity(),
                (e1, e2) -> e1, LinkedHashMap::new));

        String inSql = String.join(",", Collections.nCopies(filmsMap.size(), "?"));
        jdbcTemplate.query(
                String.format("SELECT * FROM FILM_CATEGORY AS f " +
                        "LEFT OUTER JOIN CATEGORY AS C ON f.CATEGORY_ID = c.CATEGORY_ID " +
                        "WHERE FILM_ID IN (%s) " +
                        " ORDER BY FILM_ID", inSql),
                filmsMap.keySet().toArray(),
                (rs, rowNum) -> filmsMap.get(rs.getInt("FILM_ID"))
                        .getGenres()
                        .add( new Category(rs.getInt("CATEGORY_ID"), rs.getString("CATEGORY"))));
        return new ArrayList<>(filmsMap.values());
    }// вернуть категории фильмов

    @Override
    public void createFilmCategories(Film film) {
        if (film.getGenres() != null) {
            List<Category> categories = film.getGenres().stream()
                    .distinct().collect(Collectors.toList());
            jdbcTemplate.batchUpdate(
                    "INSERT INTO FILM_CATEGORY (FILM_ID, CATEGORY_ID) VALUES (?,?)",
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, film.getId());
                            ps.setInt(2, categories.get(i).getId());
                        }

                        public int getBatchSize() {
                            return categories.size();
                        }
                    });
        }
    } // добавить категории фильма в БД

    @Override
    public void deleteFilmCategories(int id) {
        String sqlQuery = "DELETE FROM FILM_CATEGORY WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    } // удалить категории фильма из БД
}
