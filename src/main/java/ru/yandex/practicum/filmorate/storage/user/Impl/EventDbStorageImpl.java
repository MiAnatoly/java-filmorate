package ru.yandex.practicum.filmorate.storage.user.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.user.EventStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class EventDbStorageImpl implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> findAllEvent(Integer id) {
        String sql = "SELECT * FROM EVENT WHERE USER_ID = " + id +
                " ORDER BY TIMESTAMP";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeEvent(rs));
    }

    @Override
    public void create(Integer userId, Enum<EventType> eventType, Enum<Operation> operation, int entityId) {
        String sqlQuery = "INSERT INTO EVENT (TIMESTAMP, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, new Timestamp(System.currentTimeMillis()).getTime(), userId,
                    eventType.toString(), operation.toString(), entityId);
        } catch (Throwable ignored) {
            throw new NotObjectException("История не добавлена");
        }
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        Long timestamp = rs.getLong("TIMESTAMP");
        int userId = rs.getInt("USER_ID");
        String eventType = rs.getString("EVENT_TYPE");
        String operation = rs.getString("OPERATION");
        int eventId = rs.getInt("EVENT_ID");
        int entityId = rs.getInt("ENTITY_ID");
        return new Event(timestamp, userId, EventType.valueOf(eventType), Operation.valueOf(operation),
                eventId, entityId);
    }
}
