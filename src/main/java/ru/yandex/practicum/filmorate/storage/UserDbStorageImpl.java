package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

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
    } // Показать пользователей

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO USERS_FILMS (NAME, EMAIL, LOGIN, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
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
    } // Добавить пользователя

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE USERS_FILMS set NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        return user;
    } // Обнавить пользователя

    @Override
    public Optional<User> findById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS_FILMS WHERE USER_ID = ?", id);
        if (userRows.next()) {
            return Optional.of(userRows(userRows));
        } else log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    } // найти пользователя по id

    @Override
    public void createFriend(Integer friendId, Integer id) {
        String sqlQuery = "insert into FRIENDSHIP (USER_ID, FRIEND_ID, STATUS) " +
                "values (?, ?, ?)";
        boolean friendship = false;
        if(findFriends(friendId).contains(findById(id).orElse(null))) {
            friendship = true;
            String sql = "UPDATE FRIENDSHIP set STATUS = true WHERE USER_ID = ?";
            jdbcTemplate.update(sql, friendId);
        } // код для подтверждения дружбы
        jdbcTemplate.update(sqlQuery, id, friendId, friendship);
    } // Добавить друга

    @Override
    public void deleteFriend(Integer friendId, Integer id) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE  USER_ID = ? AND FRIEND_ID = ?";
        if(findFriends(friendId).contains(findById(id).orElse(null))) {
            String sql = "UPDATE FRIENDSHIP set STATUS = false WHERE USER_ID = ?";
            jdbcTemplate.update(sql, friendId);
        } // код для отклонения дружбы
        jdbcTemplate.update(sqlQuery, id, friendId);
    } // удалить друга

    @Override
    public List<User> findFriends(Integer id) {
        String sql = "SELECT * FROM USERS_FILMS AS u " +
                "WHERE u.USER_ID IN (SELECT f.FRIEND_ID FROM FRIENDSHIP AS f WHERE f.USER_ID = " + id + ")";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    } // показать друзей пользователя

    @Override
    public List<User> findOtherFriends(Integer otherId, Integer id) {
        String sql =  "SELECT * FROM USERS_FILMS AS u WHERE u.user_id IN " +
                    "(SELECT f.* FROM (SELECT friend_id FROM friendship AS f " +
                    "WHERE f.user_id = " + id + ") AS f " +
                    "INNER JOIN (SELECT friend_id " +
                    "FROM friendship AS f " +
                    "WHERE f.user_id = " + otherId + ") AS fa ON f.friend_id = fa.friend_id)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    } // общие друзья

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
