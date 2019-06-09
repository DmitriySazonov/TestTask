package com.example.testtask.domain.event;

import com.example.testtask.domain.common.Subscriber;
import com.example.testtask.domain.common.Subscription;
import com.example.testtask.other.linkedlist.Mark;
import com.example.testtask.other.linkedlist.OptimizedLinkedList;

public class EventQueue<T> {

    private OptimizedLinkedList<Subscriber<T>> subscribers = new OptimizedLinkedList<>();

    public Subscription subscribe(Subscriber<T> subscriber) {
        final Mark mark = subscribers.add(subscriber);
        return new Subscription() {
            @Override
            public void unSubscribe() {
                subscribers.remove(mark);
            }
        };
    }

    public void publish(T event) {
        for (Subscriber<T> subscriber : subscribers)
            subscriber.onNext(event);
    }
}
