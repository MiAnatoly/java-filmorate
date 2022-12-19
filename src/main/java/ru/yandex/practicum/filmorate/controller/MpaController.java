package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final FilmService service;

    @GetMapping
    public List<RatingMpa> findAll() {
        return service.findAllMpa();
    }
    // показать список рейтинга

    @GetMapping("/{id}")
    public RatingMpa findById(@PathVariable Integer id) {
        return service.findByIdMpa(id);
    }
    // показать рейтинг по ID
}
