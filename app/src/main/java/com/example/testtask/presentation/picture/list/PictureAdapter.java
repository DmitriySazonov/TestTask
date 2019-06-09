package com.example.testtask.presentation.picture.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testtask.R;
import com.example.testtask.domain.common.Subscription;
import com.example.testtask.enviroment.imageloader.ImageLoader;
import com.example.testtask.presentation.base.recycler.OnItemClickListener;
import com.example.testtask.presentation.base.recycler.RecyclerAdapter;
import com.example.testtask.presentation.picture.common.PictureItem;

public class PictureAdapter extends RecyclerAdapter<PictureItem, PictureAdapter.PictureHolder> {

    class PictureHolder extends RecyclerView.ViewHolder {

        ImageView imPicture;
        TextView tvId;
        TextView tvTitle;

        PictureHolder(@NonNull View itemView) {
            super(itemView);
            imPicture = itemView.findViewById(R.id.picture);
            tvId = itemView.findViewById(R.id.id);
            tvTitle = itemView.findViewById(R.id.title);
        }
    }

    private OnItemClickListener<PictureItem> onItemClickListener = null;

    @NonNull
    @Override
    public PictureHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_picture, viewGroup, false);
        return new PictureHolder(view);
    }

    @Override
    protected void bindViewHolder(final PictureHolder holder, final PictureItem picture) {
        holder.tvId.setText(String.valueOf(picture.id));
        holder.tvTitle.setText(picture.title);
        cancelLoad(holder.imPicture);
        Subscription subscription = ImageLoader.loadImage(picture.url, holder.imPicture);
        holder.imPicture.setTag(subscription);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(holder.imPicture, picture);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener<PictureItem> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void cancelLoad(ImageView imageView) {
        if (imageView.getTag() != null && imageView.getTag() instanceof Subscription) {
            Subscription subscription = (Subscription) imageView.getTag();
            subscription.unSubscribe();
            imageView.setTag(null);
        }
    }
}
