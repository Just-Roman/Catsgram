package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        String newEmail = user.getEmail();
        // проверяем выполнение необходимых условий
        if (newEmail == null || newEmail.isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        for (User userMap : users.values()) {
            if (userMap.getEmail().equals(newEmail)) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }
        // формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        String newEmail = newUser.getEmail();
        Long id = newUser.getId();
        // проверяем необходимые условия
        if (id == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        } else if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        } else if (!newEmail.equals(users.get(id).getEmail())) {
            for (User userMap : users.values()) {
                if (userMap.getEmail().equals(newEmail)) {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                }
            }
        }
        User oldUser = users.get(id);
        if (newUser.getUsername() != null ) oldUser.setUsername(newUser.getUsername());
        if (newEmail.isBlank()) oldUser.setEmail(newEmail);
        if (newUser.getPassword() != null) oldUser.setPassword(newUser.getPassword());
        return oldUser;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
