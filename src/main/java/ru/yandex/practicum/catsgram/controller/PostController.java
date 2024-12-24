package ru.yandex.practicum.catsgram.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> getAll(@RequestParam(defaultValue = "0") Integer from,
                                   @RequestParam(defaultValue = "10") @Positive Integer size,
                                   @RequestParam(defaultValue = "desc") String sort) {
        return postService.getAll(from, size, sort);
    }

    @GetMapping("/{postId}")
    public Post getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Post create(@Valid @RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@Valid @RequestBody Post newPost) {
        return postService.update(newPost);
    }
}