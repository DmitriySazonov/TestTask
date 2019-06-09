package com.example.testtask.presentation.base.bind;

import com.example.testtask.domain.common.Subscriber;
import com.example.testtask.domain.common.Subscription;
import com.example.testtask.other.linkedlist.Mark;
import com.example.testtask.other.linkedlist.OptimizedLinkedList;

public class SimpleValue<T> implements ObservableValue<T> {

    private OptimizedLinkedList<Subscriber<T>> subscribers = new OptimizedLinkedList<>();
    private T value;

    public SimpleValue(T initialValue) {
        value = initialValue;
    }

    public void setValue(T value) {
        this.value = value;
        notifySubscribers();
    }

    public T getValue() {
        return value;
    }

    public Subscription subscribe(Subscriber<T> subscriber) {
        final Mark mark = subscribers.add(subscriber);
        subscriber.onNext(value);
        return new Subscription() {
            @Override
            public void unSubscribe() {
                subscribers.remove(mark);
            }
        };
    }

    private void notifySubscribers() {
        for (Subscriber<T> subscriber : subscribers)
            subscriber.onNext(value);
    }
}
