package ru.yandex.practicum.catsgram.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = {"email"})
public class User {
    Long id;
    String username;
    @NotNull
    @NotBlank
    @Email
    String email;
    @NotNull
    @NotBlank
    String password;
    Instant registrationDate;

}


