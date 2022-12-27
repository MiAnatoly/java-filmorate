package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class ErrorResponse {
    String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

}
