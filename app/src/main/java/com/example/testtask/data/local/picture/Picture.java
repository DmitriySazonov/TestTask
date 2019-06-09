package com.example.testtask.data.local.picture;

public class Picture {

    public final int id;
    public final String url;
    public final String title;
    public final PictureType type;

    public Picture(int id, String url, String title, PictureType type) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.type = type;
    }
}
