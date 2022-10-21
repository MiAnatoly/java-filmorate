package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class User {
    private int id;
    @Email
    private String email;
    @EqualsAndHashCode.Exclude
    private String login;
    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private LocalDate birthday;

}
