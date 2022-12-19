package ru.yandex.practicum.filmorate.storage.user.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
public class UserDbStorageImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM USERS_FILMS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    } // показать пользователей

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO USERS_FILMS (NAME, EMAIL, LOGIN, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    } // добавить пользователя

    @Override
    public Optional<User> update(User user) {
        String sqlQuery = "UPDATE USERS_FILMS set NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM USERS_FILMS " +
                "WHERE USER_ID = ? ", user.getId());
        if (userRows.next()) {
            return Optional.of(user);
        } else log.info("Пользователь с идентификатором {} не найден.", user.getId());
        return Optional.empty();
    } // обнавить пользователя

    @Override
    public Optional<User> findById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS_FILMS WHERE USER_ID = ?", id);
        if (userRows.next()) {
            return Optional.of(userRows(userRows));
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    } // найти пользователя по id

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("USER_ID");
        String name = rs.getString("NAME");
        String login = rs.getString("LOGIN");
        String email = rs.getString("EMAIL");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        return new User(id, email, login, name, birthday);
    } // добавить в пользователя данные из БД через ResultSet

    private User userRows(SqlRowSet userRows) {
        return new User(
                userRows.getInt("USER_ID"),
                userRows.getString("EMAIL"),
                userRows.getString("LOGIN"),
                userRows.getString("NAME"),
                Objects.requireNonNull(userRows.getDate("BIRTHDAY")).toLocalDate()
        );
    } // добавить в пользователя данные из БД через SqlRowSet
}
