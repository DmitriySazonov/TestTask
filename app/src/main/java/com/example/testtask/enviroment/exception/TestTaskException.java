package com.example.testtask.enviroment.exception;

public class TestTaskException extends Exception {

    public TestTaskException(String message) {
        super(message);
    }

    public TestTaskException(String message, Throwable caused) {
        super(message, caused);
    }
}
