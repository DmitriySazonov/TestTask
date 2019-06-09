package com.example.testtask.domain.event;

import android.util.Pair;

import com.example.testtask.domain.interactor.core.base.Interactor;

public class ErrorEventQueue {

    public static EventQueue<Pair<Interactor<?, ?>, Throwable>> interactorErrors = new EventQueue<>();
}
