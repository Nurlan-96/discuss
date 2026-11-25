package com.discuss.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PostOwnershipException extends RuntimeException {

    public PostOwnershipException(String message) {
        super(message);
    }
}