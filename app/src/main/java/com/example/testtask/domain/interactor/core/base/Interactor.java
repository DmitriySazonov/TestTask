package com.example.testtask.domain.interactor.core.base;

import com.example.testtask.domain.common.Subscriber;
import com.example.testtask.domain.common.Subscription;

public interface Interactor<INPUT, OUTPUT> {

    Subscription execute(INPUT input, Subscriber<OUTPUT> subscriber);

    void cancel();
}
