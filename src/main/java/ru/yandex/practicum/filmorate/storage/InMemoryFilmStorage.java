package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Getter
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private static final LocalDate LIMIT_DATA = LocalDate.of(1895, 12, 28);

    int idFilms;

    @Override
    public List<Film> findAll() {
        List<Film> filmsList= new ArrayList<>(films.values());
        log.debug("Количество фильмов в текущий момент: {}", films.size());
        return filmsList;
    }

    @Override
    public Film create(Film film) {
        if (!(films.containsValue(film))) {
            String text = "Добавлен";
            validate(film, text);
            idFilms++;
            film.setId(idFilms);
            films.put(film.getId(), film);
        } else
            throw new RuntimeException("Фильм уже есть в базе");
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            String text = "Обновлен";
            validate(film, text);
            films.put(film.getId(), film);
        } else
            throw new NotObjectException("Фильма нет в базе");
        return film;
    }

    public void validate(Film film, String text) {
        if (film.getReleaseDate().isBefore(LIMIT_DATA))
            throw new ValidationException("Дата релиза не может быть раньше " + LIMIT_DATA);
        log.debug("{} фильм: {}", text, film.getName());
    }
}
