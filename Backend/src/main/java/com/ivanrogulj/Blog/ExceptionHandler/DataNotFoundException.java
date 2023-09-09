package com.ivanrogulj.Blog.ExceptionHandler;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}