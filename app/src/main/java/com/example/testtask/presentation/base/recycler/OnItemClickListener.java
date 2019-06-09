package com.example.testtask.presentation.base.recycler;

import android.widget.ImageView;

public interface OnItemClickListener<ITEM> {

    void onItemClick(ImageView picture, ITEM item);
}
