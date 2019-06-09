package com.example.testtask.enviroment.exception;

public class NetException extends TestTaskException {

    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable caused) {
        super(message, caused);
    }
}
