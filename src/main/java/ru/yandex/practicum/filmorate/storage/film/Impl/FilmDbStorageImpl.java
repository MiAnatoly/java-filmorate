package ru.yandex.practicum.filmorate.storage.film.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.film.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorStorage directorStorage;
    private final FilmDirectorStorage filmDirectorStorage;

    @Autowired
    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate, DirectorStorage directorStorage,
                             FilmDirectorStorage filmDirectorStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.directorStorage = directorStorage;
        this.filmDirectorStorage = filmDirectorStorage;
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM FILMS LEFT JOIN RATING_MPA RM on FILMS.RATING_ID = RM.RATING_ID";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        setDirectors(films);
        return films;
    } // показать фильмы

    private void setDirectors(List<Film> films) {
        List<Integer> filmIds = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());
        Map<Integer, List<Director>> filmDirectorsMapping = directorStorage.findByFilmIds(filmIds);
        films.forEach(f -> f.setDirectors(filmDirectorsMapping.getOrDefault(f.getId(), Collections.emptyList())));
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
        createFilmDirectors(film);
        return film;
    } // добавить фильм

    private void createFilmDirectors(Film film) {
        List<Integer> directorIds = Optional.ofNullable(film.getDirectors())
                .orElse(Collections.emptyList()).stream()
                .map(Director::getId)
                .collect(Collectors.toList());
        filmDirectorStorage.createBatch(film.getId(), directorIds);
    }

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
            filmDirectorStorage.delete(film.getId());
            createFilmDirectors(film);
            return Optional.of(film);
        } else log.info("Фильм с идентификатором {} не найден.", film.getId());
        return Optional.empty();

    } // обновить фильм

    @Override
    public void delete(int id) {
        String sql =
                "DELETE " +
                        "FROM FILMS " +
                        "WHERE FILM_ID = ?";
        int result = jdbcTemplate.update(sql, id);
        if (result == 1)
            log.info("Удалён фильм id {}", id);
        else
            throw new NotObjectException("Фильм не найден для удаления.");
    } // удалить фильм

    @Override
    public Optional<Film> findById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS" +
                " LEFT JOIN RATING_MPA RM on FILMS.RATING_ID = RM.RATING_ID WHERE FILM_ID = ? ", id);
        if (filmRows.next()) {
            Film film = filmRows(filmRows);
            film.setDirectors(directorStorage.findByFilmId(film.getId()));
            return Optional.of(film);
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    } // показать фильм по id

    @Override
    public List<Film> filmsPopular(Integer count) {
        String sql = "SELECT f.*, rm.RATING " +
                "FROM FILMS AS f LEFT OUTER JOIN like_film AS lf ON f.FILM_ID = lf.FILM_ID " +
                "LEFT JOIN RATING_MPA rm on f.RATING_ID = rm.RATING_ID " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY COUNT(lf.USER_ID) DESC " +
                "LIMIT " + count;
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        setDirectors(films);
        return films;
    } // показать попюлярные филмы

    @Override
    public List<Film> filmsByDirectorSortByLikes(int directorId) {
        String sql = "select f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, m.RATING " +
                "from FILMS f " +
                "inner join FILM_DIRECTOR d on f.FILM_ID = d.FILM_ID " +
                "left join RATING_MPA m on f.RATING_ID = m.RATING_ID " +
                "left join LIKE_FILM l on f.FILM_ID = l.FILM_ID " +
                "where d.DIRECTOR_ID = ? " +
                "group by f.FILM_ID " +
                "order by count(l.USER_ID) desc";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), directorId);
        setDirectors(films);
        return films;
    }

    @Override
    public List<Film> filmsByDirectorSortByYear(int directorId) {
        String sql = "select f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, m.RATING " +
                "from FILMS f " +
                "inner join FILM_DIRECTOR d on f.FILM_ID = d.FILM_ID " +
                "left join RATING_MPA m on f.RATING_ID = m.RATING_ID " +
                "where d.DIRECTOR_ID = ? " +
                "order by f.RELEASE_DATE";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), directorId);
        setDirectors(films);
        return films;
    }

    @Override
    public List<Film> getFilmsByQuery(String query, List<String> byParams) {
        if (query.isEmpty() && byParams.isEmpty()) {
            return filmsPopular(10);
        }
        String likeStr = "%" + query + "%";
        StringBuilder resSql = new StringBuilder();
        boolean isTwo = false;
        if (byParams.contains("director") && byParams.contains("title")) {
            resSql.append("df.DIRECTOR_NAME ILIKE ? OR f.NAME ILIKE ?");
            isTwo = true;
        } else if (byParams.contains("title")) {
            resSql.append("f.NAME ILIKE ?");
        } else if (byParams.contains("director")) {
            resSql.append("df.DIRECTOR_NAME ILIKE ?");
        }
        String sql = "SELECT f.*, rm.RATING, count(lf.USER_ID) AS mas " +
                "FROM FILMS AS f " +
                "LEFT JOIN like_film AS lf ON f.FILM_ID = lf.FILM_ID " +
                "LEFT JOIN RATING_MPA rm on f.RATING_ID = rm.RATING_ID " +
                "LEFT JOIN (SELECT fd.FILM_ID, d.DIRECTOR_NAME FROM FILM_DIRECTOR fd " +
                "LEFT JOIN DIRECTORS d on fd.DIRECTOR_ID = d.DIRECTOR_ID) AS df on f.FILM_ID = df.FILM_ID " +
                "WHERE " + resSql +
                " GROUP BY f.FILM_ID " +
                "ORDER BY count(lf.USER_ID) desc, f.FILM_ID desc";
        List<Film> films;
        if (isTwo)
            films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), likeStr, likeStr);
        else
            films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), likeStr);
        setDirectors(films);
        return films;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("FILM_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        Integer ratingId = rs.getInt("RATING_ID");
        String rate = rs.getString("RATING");
        return new Film(id, name, description, releaseDate, duration, new RatingMpa(ratingId, rate), new ArrayList<>(),
                new ArrayList<>());
    } // добавить в фильм данные из БД через ResultSet

    private Film filmRows(SqlRowSet filmRows) {
        int id = filmRows.getInt("FILM_ID");
        String name = filmRows.getString("NAME");
        String description = filmRows.getString("DESCRIPTION");
        LocalDate releaseDate = Objects.requireNonNull(filmRows.getDate("RELEASE_DATE")).toLocalDate();
        int duration = filmRows.getInt("DURATION");
        RatingMpa mpa = new RatingMpa(filmRows.getInt("RATING_ID"), filmRows.getString("RATING"));
        return new Film(id, name, description, releaseDate, duration, mpa, new ArrayList<>(), new ArrayList<>());
    } // добавить в фильм данные из БД через SqlRowSet

}
