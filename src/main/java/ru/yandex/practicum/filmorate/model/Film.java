package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    @EqualsAndHashCode.Exclude
    private String description;
    @NotNull
    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate;
    @Positive
    @EqualsAndHashCode.Exclude
    private int duration;
    private RatingMpa mpa;
    private List<Category> genres;

}
