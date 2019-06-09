package com.example.testtask.presentation.base;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class ViewModelStorage {

    private static Map<String, ViewModel> viewModels = new HashMap<>();

    public static boolean containsViewModel(String tag) {
        return viewModels.containsKey(tag);
    }

    public static void removeViewModel(String tag) {
        viewModels.remove(tag);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ViewModel> T getViewModel(String tag) {
        return (T) viewModels.get(tag);
    }

    public static void addViewModel(ViewModel viewModel, String tag) {
        viewModels.put(tag, viewModel);
    }
}
