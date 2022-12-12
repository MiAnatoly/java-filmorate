package ru.yandex.practicum.filmorate.storage.user.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class FriendshipStorageImpl implements FriendshipStorage {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFriend(Integer friendId, Integer id) {
        String sqlQuery = "insert into FRIENDSHIP (USER_ID, FRIEND_ID, STATUS) " +
                "values (?, ?, ?)";
        boolean friendship = false;
        if (findFriendsId(friendId).contains(id)) {
            friendship = true;
            String sql = "UPDATE FRIENDSHIP set STATUS = true WHERE USER_ID = ?";
            jdbcTemplate.update(sql, friendId);
        } // код для подтверждения дружбы
        validate(id, friendId);
        jdbcTemplate.update(sqlQuery, id, friendId, friendship);
    } // Добавить друга

    @Override
    public void deleteFriend(Integer friendId, Integer id) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE  USER_ID = ? AND FRIEND_ID = ?";
        if (findFriendsId(friendId).contains(id)) {
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
        String sql = "SELECT * FROM USERS_FILMS AS u WHERE u.user_id IN " +
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

    private List<Integer> findFriendsId(Integer id) {
        String sql = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = " + id;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("FRIEND_ID"));
    } // показать id друзей пользователя

    private void validate(Integer id, Integer friendId) {
        String sql = "SELECT USER_ID FROM USERS_FILMS WHERE USER_ID in (" + id + ", " + friendId + ")" +
                "GROUP BY USER_ID";
        List<Integer> ids = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("USER_ID"));
        if (!ids.contains(id))
            throw new NotObjectException("Пользователь с идентификатором {} не найден." + id);
        else if (!ids.contains(friendId))
            throw new NotObjectException("Пользователь с идентификатором {} не найден." + friendId);
    }
}
