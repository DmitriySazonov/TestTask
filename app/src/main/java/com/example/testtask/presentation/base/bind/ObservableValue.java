package com.example.testtask.presentation.base.bind;

import com.example.testtask.domain.common.Subscriber;
import com.example.testtask.domain.common.Subscription;

public interface ObservableValue<T> {

    Subscription subscribe(Subscriber<T> subscriber);
}
