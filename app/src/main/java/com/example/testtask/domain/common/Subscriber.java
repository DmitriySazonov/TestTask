package com.example.testtask.domain.common;

import android.support.annotation.Nullable;

public interface Subscriber<DATA> {

    void onNext(@Nullable DATA data);
}
