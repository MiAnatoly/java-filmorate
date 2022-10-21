package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    @EqualsAndHashCode.Exclude
    private String description;
    @NonNull
    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate;
    @Positive
    @EqualsAndHashCode.Exclude
    private int duration;


}
