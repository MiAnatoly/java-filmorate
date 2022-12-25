package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.util.List;

public interface EventStorage {

    List<Event> findAllEvent(Integer id);

    void create(Integer userId, Enum<EventType> eventType, Enum<Operation> operation, int entityId);
}
