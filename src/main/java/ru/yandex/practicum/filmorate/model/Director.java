package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level=AccessLevel.PRIVATE)
public class Director {

    @NotNull
    Integer id;

    @Size(max = 30)
    @NotBlank
    @EqualsAndHashCode.Exclude
    String name;
}
