package com.example.testtask.presentation.picture.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testtask.R;
import com.example.testtask.enviroment.imageloader.ImageLoader;
import com.example.testtask.presentation.picture.common.PictureItem;

import java.io.Serializable;

public class PictureDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Serializable serializable = getIntent().getSerializableExtra(PictureItem.TRANSITION_NAME);
        if (serializable instanceof PictureItem)
            displayPictureItem((PictureItem) serializable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayPictureItem(PictureItem pictureItem) {
        setTitle(pictureItem.title);

        ImageView picture = findViewById(R.id.picture);
        TextView id = findViewById(R.id.id);
        TextView title = findViewById(R.id.title);

        ImageLoader.loadImage(pictureItem.url, picture);
        id.setText(String.valueOf(pictureItem.id));
        title.setText(String.valueOf(pictureItem.title));
    }
}
