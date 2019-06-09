package com.example.testtask.presentation;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;

import com.example.testtask.R;
import com.example.testtask.data.local.picture.PictureType;
import com.example.testtask.presentation.picture.list.PictureFragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.testtask.data.local.picture.PictureType.CAT;
import static com.example.testtask.data.local.picture.PictureType.DOG;


public class MainActivity extends AppCompatActivity {

    private static final String CURRANT_PAGE_TAG = "CURRANT_PAGE_TAG";

    private List<PictureType> pictureTypes = Arrays.asList(CAT, DOG);

    private PictureType currentPage = CAT;

    private Map<PictureType, Bundle> savedInstanceStates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView tabBar = findViewById(R.id.tabBar);
        tabBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                onTabSelect(menuItem.getItemId());
                return true;
            }
        });
        if (savedInstanceState == null) {
            setTabPosition(currentPage);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Object object = savedInstanceState.getSerializable(CURRANT_PAGE_TAG);
        if (object instanceof PictureType)
            currentPage = (PictureType) object;
        for (PictureType type : pictureTypes) {
            Bundle cache = savedInstanceState.getBundle(type.name());
            if (cache != null)
                savedInstanceStates.put(type, cache);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRANT_PAGE_TAG, currentPage);
        for (Map.Entry<PictureType, Bundle> entry : savedInstanceStates.entrySet())
            outState.putBundle(entry.getKey().name(), entry.getValue());
    }

    private void setTabPosition(PictureType pictureType) {
        BottomNavigationView tabBar = findViewById(R.id.tabBar);
        switch (pictureType) {
            case CAT:
                tabBar.setSelectedItemId(R.id.catTab);
                break;
            case DOG:
                tabBar.setSelectedItemId(R.id.dogTab);
                break;
        }
    }

    private void onTabSelect(@IdRes int tabId) {
        PictureType pictureType = null;
        switch (tabId) {
            case R.id.catTab:
                pictureType = CAT;
                break;
            case R.id.dogTab:
                pictureType = DOG;
                break;
        }
        if (pictureType == null)
            return;
        setCurrentPage(pictureType);
    }

    private void setCurrentPage(PictureType pictureType) {
        if (!getSupportFragmentManager().getFragments().isEmpty()) {
            Fragment visibleFragment = getSupportFragmentManager().getFragments().get(0);
            Bundle cache = new Bundle();
            visibleFragment.onSaveInstanceState(cache);
            savedInstanceStates.put(currentPage, cache);
        }
        Pair<Integer, Integer> animation = fragmentTransactionAnimation(currentPage, pictureType);
        currentPage = pictureType;
        Fragment fragment = PictureFragment.newInstance(pictureType, savedInstanceStates.get(pictureType));

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(animation.first, animation.second)
                .replace(R.id.fragmentContainer, fragment, pictureType.name())
                .commit();
    }

    /**
     * @return Pair when first - inAnimation, second - outAnimation
     */
    private Pair<Integer, Integer> fragmentTransactionAnimation(PictureType current, PictureType next) {
        int currentIndex = pictureTypes.indexOf(current);
        int nextIndex = pictureTypes.indexOf(next);
        if (currentIndex == -1 || nextIndex == -1 || currentIndex == nextIndex)
            return new Pair<>(android.R.anim.fade_in, android.R.anim.fade_out);
        if (currentIndex > nextIndex)
            return new Pair<>(R.anim.slide_in_left, R.anim.slide_out_right);
        else
            return new Pair<>(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
