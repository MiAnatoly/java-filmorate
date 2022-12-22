package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Director {

    @NotNull
    private Integer id;

    @Size(max = 30)
    @NotBlank
    @EqualsAndHashCode.Exclude
    private String name;
}
