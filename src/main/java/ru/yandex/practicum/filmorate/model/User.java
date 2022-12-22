package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    @EqualsAndHashCode.Exclude
    private String login;
    @EqualsAndHashCode.Exclude
    private String name;
    @NotNull
    @PastOrPresent
    @EqualsAndHashCode.Exclude
    private LocalDate birthday;
}
