package com.example.testtask.presentation.picture.list;

import android.support.annotation.Nullable;

import com.example.testtask.data.local.picture.Picture;
import com.example.testtask.data.local.picture.PictureType;
import com.example.testtask.domain.common.Subscriber;
import com.example.testtask.domain.interactor.picture.PictureInteractor;
import com.example.testtask.presentation.base.ViewModel;
import com.example.testtask.presentation.base.bind.ObservableValue;
import com.example.testtask.presentation.base.bind.SimpleValue;
import com.example.testtask.presentation.picture.common.PictureItem;

import java.util.ArrayList;
import java.util.List;


public class PictureViewModel extends ViewModel {

    public ObservableValue<List<PictureItem>> pictures = new SimpleValue<List<PictureItem>>(new ArrayList<PictureItem>());
    public ObservableValue<Boolean> hasLoadError = new SimpleValue<Boolean>(false);
    private PictureType pictureType;

    public PictureViewModel(PictureType type) {
        pictureType = type;

        refresh();
    }

    public void refresh() {
        execute(new PictureInteractor(), pictureType, new Subscriber<List<Picture>>() {
            @Override
            public void onNext(@Nullable List<Picture> pictures) {
                ((SimpleValue<Boolean>) hasLoadError).setValue(pictures == null || pictures.isEmpty());
                setPictures(pictures != null ? mapToPictureItem(pictures) : new ArrayList<PictureItem>());
            }
        });
    }

    private void setPictures(List<PictureItem> items) {
        ((SimpleValue<List<PictureItem>>) pictures).setValue(items);
    }

    private List<PictureItem> mapToPictureItem(List<Picture> pictures) {
        List<PictureItem> picturesItems = new ArrayList<>(pictures.size());
        for (Picture picture : pictures)
            picturesItems.add(new PictureItem(picture.id, picture.url, picture.title));
        return picturesItems;
    }
}
