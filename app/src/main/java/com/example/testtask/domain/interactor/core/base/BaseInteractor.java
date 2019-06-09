package com.example.testtask.domain.interactor.core.base;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.example.testtask.domain.common.Subscriber;
import com.example.testtask.domain.common.Subscription;
import com.example.testtask.enviroment.exception.InnerException;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;

public abstract class BaseInteractor<INPUT, OUTPUT> implements Interactor<INPUT, OUTPUT> {

    private WeakReference<Subscriber<OUTPUT>> mSubscriber;
    private ErrorHandler errorHandler = InteracotrConfiguration.errorHandler;
    private ExecutorService executorService = InteracotrConfiguration.executorService;
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public BaseInteractor() {

    }

    @Nullable
    protected abstract OUTPUT onExecute(INPUT input) throws Throwable;

    @Override
    public Subscription execute(INPUT input, Subscriber<OUTPUT> subscriber) {
        mSubscriber = new WeakReference<>(subscriber);
        submitTask(executeRunnable(input));

        return new Subscription() {
            @Override
            public void unSubscribe() {
                mSubscriber = null;
            }
        };
    }

    @Override
    public void cancel() {
        mSubscriber = null;
    }

    private Runnable executeRunnable(final INPUT input) {
        return new Runnable() {
            @Override
            public void run() {
                tryExecute(input);
            }
        };
    }

    private void tryExecute(INPUT input) {
        OUTPUT output = null;
        try {
            output = onExecute(input);
        } catch (Throwable throwable) {
            submitError(throwable);
        } finally {
            notifySubscriber(output);
        }
    }

    private void notifySubscriber(@Nullable OUTPUT output) {
        if (mSubscriber == null)
            return;
        Subscriber<OUTPUT> subscriber = mSubscriber.get();
        if (subscriber != null)
            notifySubscriber(subscriber, output);
    }

    private void notifySubscriber(final Subscriber<OUTPUT> subscriber, final OUTPUT output) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                subscriber.onNext(output);
            }
        });
    }

    private void submitError(Throwable throwable) {
        if (errorHandler == null)
            return;
        errorHandler.onError(this, throwable);
    }

    private void submitTask(Runnable task) {
        if (executorService == null) {
            submitError(new InnerException("InteracotrConfiguration.executorService not initialized"));
            return;
        }
        executorService.submit(task);
    }
}
