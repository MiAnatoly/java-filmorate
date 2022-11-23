package ru.yandex.practicum.filmorate.Exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String massage) {
        super(massage);
    }
}
