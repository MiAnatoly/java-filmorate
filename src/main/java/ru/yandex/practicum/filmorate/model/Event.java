package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Event {
    Long timestamp;
    int userId;
    Enum<EventType> eventType;
    Enum<Operation> operation;
    int eventId;
    int entityId;
}
