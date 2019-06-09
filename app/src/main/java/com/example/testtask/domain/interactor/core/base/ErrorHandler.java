package com.example.testtask.domain.interactor.core.base;

public interface ErrorHandler {
    void onError(Interactor<?, ?> interactor, Throwable throwable);
}
