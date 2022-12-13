package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenresController {

    private final FilmService service;

    @GetMapping
    public List<Category> findAll() {
        return service.findAllCategory();
    }
    // показать все категории

    @GetMapping("/{id}")
    public Category findById(@PathVariable Integer id) {
        return service.findByIdCategory(id);
    }
    // показать категорию по ID
}
