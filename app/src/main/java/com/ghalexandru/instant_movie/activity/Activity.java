package com.ghalexandru.instant_movie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.fragment.MediaFragment;
import com.ghalexandru.instant_movie.util.Util;

import butterknife.BindView;


/**
 * Base activity for all activities in this project
 */
public abstract class Activity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.viewpager)
    @Nullable
    protected ViewPager viewPager;

    @Nullable
    @BindView(R.id.tab_layout)
    protected TabLayout tabLayout;


    protected SharedPreferences sharedPref;

    /**
     * Title for toolbar
     */
    protected String toolbarTitle;

    /**
     * Every activity class will need to override this method to provide
     * a contentView
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences(Util.USER_SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    /**
     * Each activity will override this method to set up their views
     */
    protected void setupViews() {
        setSupportActionBar(toolbar);
    }

    /**
     * All activities will inflate {@code /res/menu/menu_action_bar.xml}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Each activity will have different setup for this method that
     * will hide and show some items from menu
     */
    protected abstract void updateToolbarMenu();

    /**
     * Update all fragments how inherit {@link MediaFragment}
     */
    private void updateAllFragments() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment == null || !fragment.isAdded()) continue;

            if (fragment instanceof MediaFragment)
                ((MediaFragment) fragment).update();
        }
    }

    /**
     * Each activity will have some of this menu items and those items
     * will have the same functionality in every activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_grid:
                sharedPref.edit().putInt(Util.RECYCLE_VIEW_MODE, Util.RECYCLE_VIEW_GRID).apply();
                updateAllFragments();
                break;

            case R.id.action_list:
                sharedPref.edit().putInt(Util.RECYCLE_VIEW_MODE, Util.RECYCLE_VIEW_LIST).apply();
                updateAllFragments();
                break;

            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;

            case R.id.action_home:
                Intent intent = new Intent(this, MediaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }

        updateToolbarMenu();

        return super.onOptionsItemSelected(item);
    }
}
