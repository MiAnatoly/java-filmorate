package ru.yandex.practicum.filmorate.storage.film.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.film.FilmDirectorStorage;

import java.util.List;

@Component
public class FilmDirectorDbStorageImpl implements FilmDirectorStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDirectorDbStorageImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(int filmId, int directorId) {
        String sql = "insert into FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) values(:filmId, :directorId)";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmId", filmId)
                .addValue("directorId", directorId);
        jdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public void createBatch(int filmId, List<Integer> directorIds) {
        String sql = "insert into FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) values(:filmId, :directorId)";
        jdbcTemplate.batchUpdate(sql, directorIds.stream().map(directorId -> new MapSqlParameterSource()
                        .addValue("filmId", filmId)
                        .addValue("directorId", directorId))
                .toArray(SqlParameterSource[]::new));
    }

    @Override
    public void delete(int filmId) {
        String sql = "delete from FILM_DIRECTOR where FILM_ID = :filmId";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmId", filmId);
        jdbcTemplate.update(sql, namedParameters);
    }

}
