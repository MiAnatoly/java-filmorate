package ru.yandex.practicum.filmorate.storage.film.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class MpaDbStorageImpl implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<RatingMpa> findAll() {
        String sql = "SELECT * FROM RATING_MPA";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new RatingMpa(rs.getInt("RATING_ID"), rs.getString("RATING")));
    } // показать список рейтингов

    @Override
    public Optional<RatingMpa> findById(Integer id) {
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("select * from RATING_MPA where RATING_ID = ?", id);
        if (ratingRows.next()) {
            RatingMpa ratingMpa = new RatingMpa(ratingRows.getInt("RATING_ID"),
                    ratingRows.getString("RATING"));
            return Optional.of(ratingMpa);
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    } // показать рейтинг по id
}
