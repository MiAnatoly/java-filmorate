package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public List<Director> findAll() {
        return directorService.findAll();
    }

    @GetMapping("/{directorId}")
    public Director findById(@PathVariable int directorId) {
        return directorService.findById(directorId);
    }

    @PostMapping
    public Director create(@Valid @RequestBody Director director) {
        log.info("POST : create director : " + director);
        return directorService.create(director);
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        log.info("PUT : update director : " + director);
        return directorService.update(director);
    }

    @DeleteMapping("/{directorId}")
    public void delete(@PathVariable int directorId) {
        log.info("DELETE : delete director id : " + directorId);
        directorService.delete(directorId);
    }
}
