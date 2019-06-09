package com.example.testtask.presentation.picture.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.testtask.R;
import com.example.testtask.data.local.picture.PictureType;
import com.example.testtask.domain.common.Subscriber;
import com.example.testtask.enviroment.exception.InnerException;
import com.example.testtask.presentation.base.BaseFragment;
import com.example.testtask.presentation.base.recycler.OnItemClickListener;
import com.example.testtask.presentation.picture.common.PictureItem;
import com.example.testtask.presentation.picture.detail.PictureDetailActivity;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class PictureFragment extends BaseFragment<PictureViewModel> {

    private final static String PICTURE_TYPE_TAG = "PICTURE_TYPE_TAG";
    private final static String RECYCLER_SATE_TAG = "RECYCLER_SATE_TAG";

    public static PictureFragment newInstance(PictureType type, @Nullable Bundle state) {
        Bundle args = new Bundle();
        args.putSerializable(PICTURE_TYPE_TAG, type);
        if (state != null)
            args.putAll(state);
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView recyclerView = null;
    private PictureType pictureType = null;
    private PictureAdapter pictureAdapter = new PictureAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pictureAdapter.setOnItemClickListener(new OnItemClickListener<PictureItem>() {
            @Override
            public void onItemClick(ImageView picture, PictureItem pictureItem) {
                navigateToDetail(picture, pictureItem);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();
        if (context == null)
            return null;
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(pictureAdapter);

        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        frameLayout.addView(recyclerView);
        return frameLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null)
            restoreState(getArguments());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null)
            return;
        Parcelable state = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLER_SATE_TAG, state);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null)
            return;
        restoreState(savedInstanceState);
    }

    @Override
    protected PictureViewModel createViewModel() {
        return new PictureViewModel(getPictureType());
    }

    @Override
    protected String getViewModelTag() {
        return super.getViewModelTag() + getPictureType().name();
    }

    @Override
    protected void bindViewModel(View view, PictureViewModel viewModel) {
        super.bindViewModel(view, viewModel);

        bind(viewModel.pictures, new Subscriber<List<PictureItem>>() {
            @Override
            public void onNext(@Nullable List<PictureItem> pictures) {
                pictureAdapter.setItems(pictures);
            }
        });

        bind(viewModel.hasLoadError, new Subscriber<Boolean>() {
            @Override
            public void onNext(@Nullable Boolean hasError) {
                setPlaceholderVisible(hasError != null && hasError);
            }
        });
    }

    private void setPlaceholderVisible(boolean visible) {
        ViewGroup container = (ViewGroup) getView();
        if (container == null)
            return;
        if (visible) {
            if (container.findViewById(R.id.errorConnectionPlaceholder) == null) {
                LayoutInflater.from(getContext()).inflate(R.layout.place_holder_error_connection,
                        container, true);
            }
            container.findViewById(R.id.refreshButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getViewModel().refresh();
                }
            });
        } else {
            View placeholder = container.findViewById(R.id.errorConnectionPlaceholder);
            if (placeholder != null)
                container.removeView(placeholder);
        }
    }

    private void navigateToDetail(ImageView view, PictureItem pictureItem) {
        Activity activity = getActivity();
        if (activity == null)
            return;

        Intent intent = new Intent(getContext(), PictureDetailActivity.class);
        intent.putExtra(PictureItem.TRANSITION_NAME, pictureItem);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view,
                getResources().getString(R.string.shared_image_view));
        startActivity(intent, options.toBundle());
    }

    private void restoreState(Bundle state) {
        Parcelable recyclerState = state.getParcelable(RECYCLER_SATE_TAG);
        if (recyclerState != null && recyclerView.getLayoutManager() != null)
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerState);
    }

    private PictureType getPictureType() {
        if (pictureType != null)
            return pictureType;
        Bundle arguments = getArguments();
        if (arguments != null) {
            Object argument = arguments.getSerializable(PICTURE_TYPE_TAG);
            if (argument instanceof PictureType)
                pictureType = (PictureType) argument;
        }
        if (pictureType == null)
            throw new InnerException("PictureType is not define");
        return pictureType;
    }
}
