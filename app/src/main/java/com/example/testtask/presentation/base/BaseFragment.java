package com.example.testtask.presentation.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.testtask.domain.common.Subscriber;
import com.example.testtask.domain.common.Subscription;
import com.example.testtask.presentation.base.bind.ObservableValue;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseFragment<VM extends ViewModel> extends Fragment {

    private VM viewModel = null;
    private List<Subscription> subscriptions = new LinkedList<>();
    private Snackbar showingMessage = null;

    protected abstract VM createViewModel();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!ViewModelStorage.containsViewModel(getViewModelTag()))
            ViewModelStorage.addViewModel(createViewModel(), getViewModelTag());
        viewModel = ViewModelStorage.getViewModel(getViewModelTag());

        bindError();
        bindViewModel(view, viewModel);
    }

    @Override
    public void onDestroyView() {
        unBindViewModel(getView(), viewModel);
        for (Subscription subscription : subscriptions)
            subscription.unSubscribe();
        subscriptions.clear();
        super.onDestroyView();
    }

    protected void bindViewModel(View view, VM viewModel) {
        // for override
    }

    protected void unBindViewModel(View view, VM viewModel) {
        // for override
    }

    protected VM getViewModel() {
        return viewModel;
    }

    protected String getViewModelTag() {
        return getClass().getName();
    }

    protected <T> void bind(ObservableValue<T> value, Subscriber<T> subscriber) {
        subscriptions.add(value.subscribe(subscriber));
    }

    private void bindError() {
        bind(viewModel.errorMessage, new Subscriber<String>() {
            @Override
            public void onNext(@Nullable String message) {
                if (showingMessage != null && showingMessage.isShown())
                    showingMessage.dismiss();
                showingMessage = null;
                if (getView() != null && message != null)
                    showingMessage = Snackbar.make(getView(), message, Snackbar.LENGTH_INDEFINITE);
                if (showingMessage != null)
                    showingMessage.show();
            }
        });
    }
}
