package com.discuss.post.controller;

import com.discuss.auth.security.CustomUserPrincipal;
import com.discuss.post.dto.CreatePostRequest;
import com.discuss.post.dto.PostResponse;
import com.discuss.post.dto.UpdatePostRequest;
import com.discuss.post.service.PostService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            Authentication authentication,
            @Valid @RequestBody CreatePostRequest request) {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        PostResponse response = postService.createPost(
                principal.getId(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable String id) {

        return ResponseEntity.ok(postService.getPost(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable String id,
            Authentication authentication,
            @Valid @RequestBody UpdatePostRequest request) {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        PostResponse response = postService.updatePost(
                id,
                principal.getId(),
                request
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable String id,
            Authentication authentication) {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        postService.deletePost(id, principal.getId());

        return ResponseEntity.noContent().build();
    }
}