package ru.yandex.practicum.catsgram.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = {"id"})
public class Post {
    Long id;
    @NotNull
    Long authorId;
    Instant postDate;
    @NotNull
    @NotBlank
    String description;

}
