package com.example.testtask.presentation.base.recycler;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerAdapter<ITEM, VM extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VM> {

    private List<ITEM> items = new ArrayList<>();
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    protected abstract void bindViewHolder(VM holder, ITEM item);

    @Override
    public final int getItemCount() {
        return items.size();
    }

    @Override
    public final void onBindViewHolder(@NonNull VM holder, int position) {
        bindViewHolder(holder, items.get(position));
    }

    public void setItems(List<ITEM> items) {
        List<ITEM> oldItems = this.items;
        this.items = items;
        itemsChanged(oldItems, items);
    }

    protected boolean equalsItems(ITEM item1, ITEM item2) {
        return item1.equals(item2);
    }

    private void itemsChanged(final List<ITEM> oldItems, final List<ITEM> newItems) {
        try {
            DiffUtil.calculateDiff(diffCallback(oldItems, newItems)).dispatchUpdatesTo(this);
        } catch (IllegalStateException e) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    itemsChanged(oldItems, newItems);
                }
            });
        }
    }

    private DiffUtil.Callback diffCallback(final List<ITEM> oldList, final List<ITEM> newList) {
        return new DiffUtil.Callback() {

            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldPosition, int newPosition) {
                return oldList.get(oldPosition) == newList.get(newPosition);
            }

            @Override
            public boolean areContentsTheSame(int oldPosition, int newPosition) {
                return equalsItems(oldList.get(oldPosition), newList.get(newPosition));
            }
        };
    }
}
