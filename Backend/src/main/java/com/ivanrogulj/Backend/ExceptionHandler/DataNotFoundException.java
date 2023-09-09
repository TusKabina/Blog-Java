package com.ivanrogulj.Backend.ExceptionHandler;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}