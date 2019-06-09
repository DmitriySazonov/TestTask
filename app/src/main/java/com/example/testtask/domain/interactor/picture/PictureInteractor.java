package com.example.testtask.domain.interactor.picture;

import com.example.testtask.data.local.picture.Picture;
import com.example.testtask.data.local.picture.PictureType;
import com.example.testtask.data.mapper.picture.PictureMapper;
import com.example.testtask.data.net.picture.PictureResponse;
import com.example.testtask.domain.interactor.core.net.NetInteractorBase;
import com.example.testtask.enviroment.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;

public class PictureInteractor extends NetInteractorBase<PictureType, PictureResponse, List<Picture>> {

    @Override
    protected final Call<PictureResponse> makeRequest(PictureType pictureType) {
        return RetrofitService.getInstance().getRepository().pictures(pictureType.name().toLowerCase());
    }

    @Override
    protected final List<Picture> mapping(PictureType pictureType, PictureResponse pictureResponse) {
        return new PictureMapper(pictureType).map(pictureResponse);
    }
}
