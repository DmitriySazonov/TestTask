package com.example.testtask.presentation.base;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.example.testtask.domain.common.Subscriber;
import com.example.testtask.domain.common.Subscription;
import com.example.testtask.domain.event.ErrorEventQueue;
import com.example.testtask.domain.interactor.core.base.Interactor;
import com.example.testtask.enviroment.exception.NetException;
import com.example.testtask.other.linkedlist.Mark;
import com.example.testtask.other.linkedlist.OptimizedLinkedList;
import com.example.testtask.presentation.base.bind.ObservableValue;
import com.example.testtask.presentation.base.bind.SimpleValue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ViewModel {

    /**
     * message may be null
     */
    public final ObservableValue<String> errorMessage = new SimpleValue<>(null);
    public final ObservableValue<Boolean> isLoading = new SimpleValue<>(false);

    protected Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    protected long errorMessageDuration = TimeUnit.SECONDS.toMillis(2);

    private List<Subscription> subscriptions = new LinkedList<>();
    private OptimizedLinkedList<Interactor<?, ?>> executingInteractors = new OptimizedLinkedList<>();

    public ViewModel() {
        subscribeOnError();
    }

    protected <I, O> void execute(Interactor<I, O> interactor, I input, final Subscriber<O> subscriber) {
        final Mark mark = executingInteractors.add(interactor);
        interactor.execute(input, new Subscriber<O>() {
            @Override
            public void onNext(@Nullable O output) {
                executingInteractors.remove(mark);
                subscriber.onNext(output);
            }
        });
    }

    public void destroy() {
        for (Interactor<?, ?> interactor : executingInteractors)
            interactor.cancel();
        executingInteractors.clear();

        for (Subscription subscription : subscriptions)
            subscription.unSubscribe();
        subscriptions.clear();
    }

    protected void onHandleError(Throwable throwable) {
        final SimpleValue<String> value = (SimpleValue<String>) errorMessage;
        if (throwable instanceof NetException)
            value.setValue("Проверьте подключение к интернету.");
        mainThreadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                value.setValue(null);
            }
        }, errorMessageDuration);
    }

    private void handleError(Interactor<?, ?> interactor, Throwable throwable) {
        if (executingInteractors.contains(interactor))
            onHandleError(throwable);
    }

    private void subscribeOnError() {
        Subscription subscription = ErrorEventQueue.interactorErrors.subscribe(new Subscriber<Pair<Interactor<?, ?>, Throwable>>() {
            @Override
            public void onNext(@Nullable Pair<Interactor<?, ?>, Throwable> interactorThrowablePair) {
                if (interactorThrowablePair != null)
                    handleError(interactorThrowablePair.first, interactorThrowablePair.second);
            }
        });
        subscriptions.add(subscription);
    }
}
