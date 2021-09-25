package com.example.realworld.application.favorites.exception;

public class NotFoundFavoriteArticleException extends RuntimeException {
    public NotFoundFavoriteArticleException() {
    }

    public NotFoundFavoriteArticleException(String message) {
        super(message);
    }
}
