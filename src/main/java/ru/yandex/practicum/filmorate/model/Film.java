package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Film {
    @EqualsAndHashCode.Exclude
    int id;
    @NotBlank
    String name;
    @NotBlank
    @Size(max = 200)
    @EqualsAndHashCode.Exclude
    String description;
    @NotNull
    @EqualsAndHashCode.Exclude
    LocalDate releaseDate;
    @Positive
    @EqualsAndHashCode.Exclude
    int duration;
    @NotNull
    RatingMpa mpa;
    List<Category> genres;
    List<Director> directors;
}
