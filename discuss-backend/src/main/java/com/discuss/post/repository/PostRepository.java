package com.discuss.post.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.discuss.post.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {
}
