package com.example.testtask;

import android.app.Application;
import android.util.Pair;

import com.example.testtask.domain.event.ErrorEventQueue;
import com.example.testtask.domain.interactor.core.base.ErrorHandler;
import com.example.testtask.domain.interactor.core.base.InteracotrConfiguration;
import com.example.testtask.domain.interactor.core.base.Interactor;
import com.example.testtask.enviroment.Constants;
import com.example.testtask.enviroment.imageloader.ImageLoader;
import com.example.testtask.enviroment.retrofit.RetrofitService;

import java.util.concurrent.Executors;

public class TestTaskApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initRetrofit();
        initInteractors();
        initImageLoader();
    }

    private void initRetrofit() {
        RetrofitService.init(this, Constants.HOST_API);
    }

    private void initInteractors() {
        InteracotrConfiguration.executorService = Executors.newSingleThreadExecutor();
        InteracotrConfiguration.errorHandler = new ErrorHandler() {
            @Override
            public void onError(Interactor<?, ?> interactor, Throwable throwable) {
                ErrorEventQueue.interactorErrors.publish(new Pair<Interactor<?, ?>, Throwable>(interactor, throwable));
            }
        };
    }

    private void initImageLoader() {
        ImageLoader.initialize(this);
    }
}
