package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorStorage directorStorage;

    @Override
    public List<Director> findAll() {
        return directorStorage.findAll();
    }

    @Override
    public Director findById(int directorId) {
        return directorStorage.findById(directorId)
                .orElseThrow(() -> new EntityNotFoundException("No director entity with id : " + directorId));
    }

    @Override
    public Director create(Director director) {
        return directorStorage.create(director);
    }

    @Override
    public Director update(Director director) {
        return directorStorage.update(director);
    }

    @Override
    public void delete(int directorId) {
        directorStorage.delete(directorId);
    }
}
