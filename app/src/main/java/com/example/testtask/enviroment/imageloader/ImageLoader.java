package com.example.testtask.enviroment.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.testtask.R;
import com.example.testtask.domain.common.Subscription;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.lang.ref.WeakReference;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class ImageLoader {

    private static int placeholder = R.drawable.ic_picture_placeholder;

    public static void initialize(Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "PicassoCache");
        int cacheSize = 10 * 1024 * 1024; // 10MB

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(new Cache(httpCacheDirectory, cacheSize))
                .build();

        Picasso.Builder builder = new Picasso.Builder(context)
                .memoryCache(new LruCache(cacheSize))
                .downloader(new OkHttp3Downloader(client));

        Picasso.setSingletonInstance(builder.build());
    }

    public static Subscription loadImage(String url, ImageView imageView) {
        final WeakReference<ImageView> reference = new WeakReference<>(imageView);
        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ImageView image = reference.get();
                if (image != null)
                    image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                ImageView image = reference.get();
                if (image != null)
                    image.setImageResource(placeholder);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        return new Subscription() {
            @Override
            public void unSubscribe() {
                reference.clear();
            }
        };
    }
}
