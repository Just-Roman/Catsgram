package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final UserService userService;

    private final Map<Long, Post> posts = new HashMap<>();


    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> getAll(Integer from, Integer size, String sort) {
        List<Post> sortedPosts = posts.values().stream()
                .sorted(Comparator.comparing(Post::getPostDate))
                .collect(Collectors.toCollection(ArrayList::new));

        if (getTypeSort(sort).equals("DESCENDING")) {
            sortedPosts = sortedPosts.reversed();
        }
        List<Post> returnablePost = new ArrayList<>();

        if (size < 0) {
            throw new ParameterNotValidException("error : " + size,
                    " Некорректный размер выборки. Размер должен быть больше нуля");
        }
        if (from < 0) {
            throw new ParameterNotValidException("error : " + from,
                    " Некорректный размер выборки. Начало должно быть больше нуля");
        }

        for (int i = from; i < from + size; i++) {
            if (sortedPosts.size() < i + 1) break;
            returnablePost.add(sortedPosts.get(i));
        }
        return returnablePost;

    }


    private String getTypeSort(String sort) {
        return switch (sort.toLowerCase()) {
            case "ascending", "asc" -> "ASCENDING";
            case "descending", "desc" -> "DESCENDING";
            default -> throw new ParameterNotValidException("error : " + sort,
                    " Допустимые варианты сортировки: asc(ascending), desc(descending)");
        };
    }


    public Post getPostById(Long id) {
        if (id == null) {
            throw new ConditionsNotMetException("id должен быть числом");
        }
        if (posts.containsKey(id)) {
            return posts.get(id);
        } else {
            throw new ConditionsNotMetException("Пост с id = " + id + " не найден.");
        }
    }

    public Post create(Post post) {
        Long authorId = post.getAuthorId();
        if (userService.findUserById(authorId).isEmpty()) {
            throw new ConditionsNotMetException("Автор с id = " + authorId + " не найден");
        }
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
