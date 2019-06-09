package com.example.testtask.domain.interactor.core.net;

import android.support.annotation.Nullable;

import com.example.testtask.domain.interactor.core.base.BaseInteractor;
import com.example.testtask.enviroment.exception.NetException;

import retrofit2.Call;
import retrofit2.Response;

public abstract class NetInteractorBase<INPUT, RESPONSE, OUTPUT> extends BaseInteractor<INPUT, OUTPUT> {

    protected abstract Call<RESPONSE> makeRequest(INPUT input) throws Throwable;

    protected abstract OUTPUT mapping(INPUT input, RESPONSE response) throws Throwable;

    @Nullable
    @Override
    protected final OUTPUT onExecute(INPUT input) throws Throwable {
        try {
            Call<RESPONSE> request = makeRequest(input);
            Response<RESPONSE> response = request.execute();

            if (response.isSuccessful())
                return mapping(input, response.body());

            throw new NetException("Failed request -> " + request.request().toString());
        } catch (Throwable throwable) {
            throw new NetException("An error occurred in " + getClass().getName(), throwable);
        }
    }
}
