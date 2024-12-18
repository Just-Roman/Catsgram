package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = {"id"})
public class Post {
    Long id;
    Long authorId;
    Instant postDate;
    String description;

}
