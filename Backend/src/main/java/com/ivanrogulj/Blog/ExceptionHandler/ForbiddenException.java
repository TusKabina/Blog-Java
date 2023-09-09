package com.ivanrogulj.Blog.ExceptionHandler;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}