package com.discuss.post.service;

import java.net.URI;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.discuss.post.dto.CreatePostRequest;
import com.discuss.post.dto.PostResponse;
import com.discuss.post.dto.UpdatePostRequest;
import com.discuss.post.exception.PostNotFoundException;
import com.discuss.post.exception.PostOwnershipException;
import com.discuss.post.model.Post;
import com.discuss.post.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse createPost(
            String authorId,
            CreatePostRequest request) {

        Post post = new Post();

        post.setTitle(request.getTitle().trim());
        post.setContent(normalizeOptional(request.getContent()));
        post.setUrl(normalizeOptional(request.getUrl()));
        post.setImageUrl(normalizeOptional(request.getImageUrl()));

        validatePostContent(
                post.getContent(),
                post.getUrl(),
                post.getImageUrl()
        );

        validateUrl(post.getUrl());
        validateUrl(post.getImageUrl());

        post.setAuthorId(authorId);
        post.setScore(0);
        post.setCommentCount(0);

        Instant now = Instant.now();

        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        Post savedPost = postRepository.save(post);

        return mapToResponse(savedPost);
    }

    public PostResponse getPost(String postId) {

        Post post = findPost(postId);

        return mapToResponse(post);
    }

    public PostResponse updatePost(
            String postId,
            String userId,
            UpdatePostRequest request) {

        Post post = findPost(postId);

        verifyOwnership(post, userId);

        if (request.getTitle() != null) {
            post.setTitle(request.getTitle().trim());
        }

        if (request.getContent() != null) {
            post.setContent(normalizeOptional(request.getContent()));
        }

        if (request.getUrl() != null) {
            post.setUrl(normalizeOptional(request.getUrl()));
        }

        if (request.getImageUrl() != null) {
            post.setImageUrl(normalizeOptional(request.getImageUrl()));
        }

        validatePostContent(
                post.getContent(),
                post.getUrl(),
                post.getImageUrl()
        );

        validateUrl(post.getUrl());
        validateUrl(post.getImageUrl());

        post.setUpdatedAt(Instant.now());

        Post updatedPost = postRepository.save(post);

        return mapToResponse(updatedPost);
    }

    public void deletePost(String postId, String userId) {

        Post post = findPost(postId);

        verifyOwnership(post, userId);

        postRepository.delete(post);
    }

    private Post findPost(String postId) {

        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new PostNotFoundException("Post not found"));
    }

    private void verifyOwnership(Post post, String userId) {

        if (!post.getAuthorId().equals(userId)) {
            throw new PostOwnershipException(
                    "You are not allowed to modify this post"
            );
        }
    }

    private void validatePostContent(
            String content,
            String url,
            String imageUrl) {

        boolean hasContent = content != null;
        boolean hasUrl = url != null;
        boolean hasImage = imageUrl != null;

        if (!hasContent && !hasUrl && !hasImage) {
            throw new IllegalArgumentException(
                    "A post must contain text, a URL, or an image"
            );
        }
    }

    private void validateUrl(String value) {

        if (value == null) {
            return;
        }

        try {
            URI uri = URI.create(value);

            String scheme = uri.getScheme();

            if (!uri.isAbsolute()
                    || uri.getHost() == null
                    || scheme == null
                    || !(scheme.equalsIgnoreCase("http")
                    || scheme.equalsIgnoreCase("https"))) {

                throw new IllegalArgumentException(
                        "Invalid URL: " + value
                );
            }

        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid URL: " + value
            );
        }
    }

    private String normalizeOptional(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private PostResponse mapToResponse(Post post) {

        PostResponse response = new PostResponse();

        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setUrl(post.getUrl());
        response.setImageUrl(post.getImageUrl());
        response.setAuthorId(post.getAuthorId());
        response.setScore(post.getScore());
        response.setCommentCount(post.getCommentCount());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        return response;
    }
}