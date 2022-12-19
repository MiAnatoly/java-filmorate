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
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFriend(Integer friendId, Integer id) {
        String sqlQuery = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) " +
                "VALUES (?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, id, friendId);
        } catch (Throwable ignored) {
          throw new NotObjectException("Пользователь с идентификатором "+ id +" или "+ friendId +" не найден.");
        }
    } // добавить друга

    @Override
    public void deleteFriend(Integer friendId, Integer id) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE  USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    } // удалить друга

    @Override
    public List<User> findFriends(Integer id) {
        String sql = "SELECT * FROM USERS_FILMS, FRIENDSHIP " +
                "WHERE USERS_FILMS.USER_ID = FRIENDSHIP.FRIEND_ID AND FRIENDSHIP.USER_ID = " + id;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    } // показать друзей пользователя

    @Override
    public List<User> findOtherFriends(Integer otherId, Integer id) {
        String sql = "SELECT * FROM USERS_FILMS u, FRIENDSHIP f, FRIENDSHIP o " +
                "WHERE u.USER_ID = f.FRIEND_ID " +
                "AND u.USER_ID = o.FRIEND_ID " +
                "AND f.USER_ID = "+ id +" AND o.USER_ID = "+ otherId;
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

}
