package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Event {
    private Long timestamp;
    private int userId;
    private Enum<EventType> eventType;
    private Enum<Operation> operation;
    private int eventId;
    private int entityId;
}
