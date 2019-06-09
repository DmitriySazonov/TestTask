package com.example.testtask.enviroment.exception;

public class InnerException extends RuntimeException {
    public InnerException(String message) {
        super(message);
    }

    public InnerException(String message, Throwable caused) {
        super(message, caused);
    }
}
