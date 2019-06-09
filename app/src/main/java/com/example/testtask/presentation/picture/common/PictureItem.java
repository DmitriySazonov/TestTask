package com.example.testtask.presentation.picture.common;

import java.io.Serializable;

public class PictureItem implements Serializable {

    public static String TRANSITION_NAME = PictureItem.class.getSimpleName();

    public final int id;
    public final String url;
    public final String title;

    public PictureItem(int id, String url, String title) {
        this.id = id;
        this.url = url;
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PictureItem))
            return false;
        PictureItem picture = (PictureItem) obj;
        return picture.id == id && picture.url.equals(url) &&picture.title.equals(title);
    }
}
