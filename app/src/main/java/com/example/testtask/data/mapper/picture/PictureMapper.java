package com.example.testtask.data.mapper.picture;

import android.support.annotation.NonNull;

import com.example.testtask.data.local.picture.Picture;
import com.example.testtask.data.local.picture.PictureType;
import com.example.testtask.data.mapper.Mapper;
import com.example.testtask.data.net.picture.NetPicture;
import com.example.testtask.data.net.picture.PictureResponse;

import java.util.ArrayList;
import java.util.List;

public class PictureMapper implements Mapper<PictureResponse, List<Picture>> {

    private final PictureType pictureType;

    public PictureMapper(PictureType pictureType) {
        this.pictureType = pictureType;
    }

    @NonNull
    @Override
    public List<Picture> map(PictureResponse pictureResponse) {
        List<Picture> pictures = new ArrayList<>(pictureResponse.data.size());
        for (int i = 0; i < pictureResponse.data.size(); i++) {
            NetPicture picture = pictureResponse.data.get(i);
            pictures.add(new Picture(i, picture.url, picture.title, pictureType));
        }
        return pictures;
    }
}
