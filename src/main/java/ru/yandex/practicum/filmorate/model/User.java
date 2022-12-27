package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User {
    int id;
    @NotNull
    @Email
    String email;
    @NotNull
    @NotBlank
    @EqualsAndHashCode.Exclude
    String login;
    @EqualsAndHashCode.Exclude
    String name;
    @NotNull
    @PastOrPresent
    @EqualsAndHashCode.Exclude
    LocalDate birthday;
}
