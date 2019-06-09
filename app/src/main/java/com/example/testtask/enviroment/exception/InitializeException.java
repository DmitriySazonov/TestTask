package com.example.testtask.enviroment.exception;

public class InitializeException extends TestTaskException {

    public InitializeException(String message) {
        super(message);
    }

    public InitializeException(String message, Throwable caused) {
        super(message, caused);
    }
}
