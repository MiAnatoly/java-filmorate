package ru.yandex.practicum.filmorate.storage.film.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.DirectorStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DirectorDbStorageImpl implements DirectorStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDbStorageImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> findAll() {
        String sql = "select DIRECTOR_ID, DIRECTOR_NAME from DIRECTORS";
        return jdbcTemplate.query(sql, this::mapRowToDirector);
    }

    private Director mapRowToDirector(ResultSet rs, int rowNum) throws SQLException {
        int directorId = rs.getInt("DIRECTOR_ID");
        String directorName = rs.getString("DIRECTOR_NAME");
        return new Director(directorId, directorName);
    }

    @Override
    public Optional<Director> findById(int id) {
        String sql = "select DIRECTOR_ID, DIRECTOR_NAME from DIRECTORS where DIRECTOR_ID = :directorId";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("directorId", id);
        return jdbcTemplate.query(sql, namedParameters, this::mapRowToDirector).stream()
                .findFirst();
    }

    @Override
    public Director create(Director director) {
        String sql = "insert into DIRECTORS (DIRECTOR_NAME) values (:directorName)";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("directorName", director.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, namedParameters, keyHolder, new String[]{"DIRECTOR_ID"});
        int directorId = Optional.ofNullable(keyHolder.getKey()).orElse(0).intValue();
        director.setId(directorId);
        return director;
    }

    @Override
    public Director update(Director director) {
        String sql = "update DIRECTORS set DIRECTOR_NAME = :directorName where DIRECTOR_ID = :directorId";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("directorId", director.getId())
                .addValue("directorName", director.getName());
        int rowsAffected = jdbcTemplate.update(sql, namedParameters);
        if (rowsAffected == 0) {
            throw new EntityNotFoundException("No entity director with id : " + director.getId());
        }
        return director;
    }

    @Override
    public void delete(int id) {
        String sql = "delete from DIRECTORS where DIRECTOR_ID = :directorId";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("directorId", id);
        int rowsAffected = jdbcTemplate.update(sql, namedParameters);
        if (rowsAffected == 0) {
            throw new EntityNotFoundException("No entity director with id : " + id);
        }
    }

    @Override
    public List<Director> findByFilmId(int filmId) {
        String sql = "select d.DIRECTOR_ID, d.DIRECTOR_NAME from FILM_DIRECTOR f, DIRECTORS d " +
                "where f.FILM_ID = :filmId and f.DIRECTOR_ID = d.DIRECTOR_ID";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmId", filmId);
        return jdbcTemplate.query(sql, namedParameters, this::mapRowToDirector);
    }

    @Override
    public Map<Integer, List<Director>> findByFilmIds(List<Integer> filmIds) {
        String sql = "select f.FILM_ID, d.DIRECTOR_ID, d.DIRECTOR_NAME " +
                "from FILM_DIRECTOR f, DIRECTORS d " +
                "where f.DIRECTOR_ID = d.DIRECTOR_ID and f.FILM_ID in (:filmIds) " +
                "group by f.FILM_ID, d.DIRECTOR_ID";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("filmIds", filmIds);
        List<Map<String, Object>> data = jdbcTemplate.query(sql, namedParameters, this::mapDirector);
        return data.stream().collect(Collectors.groupingBy(m -> (Integer) m.get("filmId"),
                Collectors.mapping(m -> new Director((Integer) m.get("directorId"), (String) m.get("directorName")),
                        Collectors.toList())));
    }

    private Map<String, Object> mapDirector(ResultSet rs, int rowNum) throws SQLException {
        int filmId = rs.getInt("FILM_ID");
        int directorId = rs.getInt("DIRECTOR_ID");
        String directorName = rs.getString("DIRECTOR_NAME");
        return Map.ofEntries(Map.entry("filmId", filmId), Map.entry("directorId", directorId),
                Map.entry("directorName", directorName));
    }
}
