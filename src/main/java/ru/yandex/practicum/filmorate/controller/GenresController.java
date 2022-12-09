package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Validated
public class GenresController {

    private final FilmService service;

    @GetMapping
    public List<Category> findAllCategory() {
        return service.findAllCategory();
    }
    // показать всех пользователей

    @GetMapping("/{id}")
    public Category findById(@PathVariable Integer id) {
        return service.findByIdCategory(id);
    }
    // показать пользователя по ID
}
