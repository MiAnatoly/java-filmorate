package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Validated
public class MpaController {

    private final FilmService service;

    @GetMapping
    public List<RatingMpa> findAllMpa() {
        return service.findAllMpa();
    }
    // показать всех пользователей

    @GetMapping("/{id}")
    public RatingMpa findByIdMpa(@PathVariable Integer id) {
        return service.findByIdMpa(id);
    }
    // показать пользователя по ID
}
