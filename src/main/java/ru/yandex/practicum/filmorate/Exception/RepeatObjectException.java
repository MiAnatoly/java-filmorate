package ru.yandex.practicum.filmorate.Exception;

public class RepeatObjectException extends RuntimeException{
    public RepeatObjectException(String massage) {
        super(massage);
    }
}
