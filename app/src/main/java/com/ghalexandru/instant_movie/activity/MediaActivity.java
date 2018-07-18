package com.ghalexandru.instant_movie.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.fragment.CharacterFragment;
import com.ghalexandru.instant_movie.fragment.MediaList;
import com.ghalexandru.instant_movie.util.MovieType;
import com.ghalexandru.instant_movie.util.TvShowType;
import com.ghalexandru.instant_movie.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity class used for discover movies or tv shows
 */
public class MediaActivity extends Activity {

    private final static String TAG = MediaActivity.class.getSimpleName();


    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private static final String KEY_VIEW_PAGER_POSITION = "viewpager_position";
    private static final String KEY_TOOLBAR_TITLE = "toolbar_title";
    private static final String KEY_NAV_VIEW_POSITION = "nav_view_position";

    private final static int NAV_VIEW_ITEM_MOVIES = 0;
    private final static int NAV_VIEW_ITEM_TV_SHOWS = 1;
    private final static int NAV_VIEW_ITEM_PEOPLE = 2;

    private static final int NAV_VIEW_ITEM_CUSTOM_LIST = 3;
    private static final int NAV_VIEW_ITEM_WATCHLIST = 4;
    private static final int NAV_VIEW_ITEM_FAVORITES = 5;

    /**
     * Field used to store position for {@link NavigationView}
     */
    private int navViewPosition = NAV_VIEW_ITEM_MOVIES;

    /**
     * Field used to store position for {@link android.support.v4.view.ViewPager}
     */
    private int viewPagerPosition = 0;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        ButterKnife.bind(this);

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        navigationView.getMenu().getItem(navViewPosition).setChecked(true);
        viewPagerAdapter.notifyDataSetChanged();

        if (viewPager != null)
            viewPager.setCurrentItem(viewPagerPosition);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        viewPagerPosition = viewPager != null ? viewPager.getCurrentItem() : 0;

        outState.putInt(KEY_NAV_VIEW_POSITION, navViewPosition);
        outState.putInt(KEY_VIEW_PAGER_POSITION, viewPagerPosition);
        outState.putString(KEY_TOOLBAR_TITLE, toolbarTitle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            navViewPosition = savedInstanceState.getInt(KEY_NAV_VIEW_POSITION, NAV_VIEW_ITEM_MOVIES);
            viewPagerPosition = savedInstanceState.getInt(KEY_VIEW_PAGER_POSITION, 0);
            toolbarTitle = savedInstanceState.getString(KEY_TOOLBAR_TITLE, getString(R.string.movies_name));
        }
    }

    /**
     * Listener for {@link #navigationView}
     */
    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            item.setChecked(true);

            /**
             * Update {@link toolbarTitle} and {@link navViewPosition} based on current
             * item selected from {@link navigationView}
             */
            switch (item.getItemId()) {
                case R.id.item_movies:
                    toolbarTitle = getString(R.string.movies_name);
                    navViewPosition = NAV_VIEW_ITEM_MOVIES;
                    break;

                case R.id.item_shows:
                    toolbarTitle = getString(R.string.shows_name);
                    navViewPosition = NAV_VIEW_ITEM_TV_SHOWS;
                    break;

                case R.id.item_persons:
                    toolbarTitle = getString(R.string.persons_name);
                    navViewPosition = NAV_VIEW_ITEM_PEOPLE;
                    break;

                case R.id.item_custom_list:
                    toolbarTitle = getString(R.string.custom_list);
                    navViewPosition = NAV_VIEW_ITEM_CUSTOM_LIST;
                    break;
                case R.id.item_watchlist:
                    toolbarTitle = getString(R.string.watchlist);
                    navViewPosition = NAV_VIEW_ITEM_WATCHLIST;
                    break;
                case R.id.item_favorites:
                    toolbarTitle = getString(R.string.favorites);
                    navViewPosition = NAV_VIEW_ITEM_FAVORITES;
                    break;

                default:
                    break;

            }

            viewPagerAdapter.update();
            viewPagerPosition = 0;

            if (viewPager != null)
                viewPager.setCurrentItem(viewPagerPosition);

            updateToolbarMenu();

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
    };


    /**
     * Setup {@link #navigationView} and {@link #viewPagerAdapter}
     */
    @Override
    protected void setupViews() {
        super.setupViews();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        updateToolbarMenu();
        return true;
    }

    @Override
    protected void updateToolbarMenu() {
        int viewMode = sharedPref.getInt(Util.RECYCLE_VIEW_MODE, Util.RECYCLE_VIEW_LIST);

        Menu toolbarMenu = toolbar.getMenu();

        toolbarMenu.findItem(R.id.action_home).setVisible(false);

        switch (navViewPosition) {
            case NAV_VIEW_ITEM_MOVIES:
            case NAV_VIEW_ITEM_TV_SHOWS:

                tabLayout.setVisibility(View.VISIBLE);
                toolbarMenu.findItem(R.id.action_list).setVisible(viewMode == Util.RECYCLE_VIEW_GRID);
                toolbarMenu.findItem(R.id.action_grid).setVisible(viewMode == Util.RECYCLE_VIEW_LIST);

                break;
            case NAV_VIEW_ITEM_PEOPLE:

                tabLayout.setVisibility(View.GONE);
                toolbarMenu.findItem(R.id.action_list).setVisible(false);
                toolbarMenu.findItem(R.id.action_grid).setVisible(false);

                break;

            case NAV_VIEW_ITEM_CUSTOM_LIST:
            case NAV_VIEW_ITEM_FAVORITES:
            case NAV_VIEW_ITEM_WATCHLIST:

                Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();

                tabLayout.setVisibility(View.GONE);
                toolbarMenu.findItem(R.id.action_list).setVisible(false);
                toolbarMenu.findItem(R.id.action_grid).setVisible(false);

                break;
        }

        toolbar.setTitle(toolbarTitle == null || toolbarTitle.equals("") ? getString(R.string.movies_name) : toolbarTitle);
    }


    /**
     * Viewpager adapter used for {@link #viewPager}
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        /**
         * Field used to know when {@link #navigationView} change position
         */
        private boolean shouldChange;

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        /**
         * Method used when {@link #navigationView} change position
         * to update {@link #viewPager}
         */
        public void update() {
            shouldChange = true;
            notifyDataSetChanged();
        }

        /**
         * Return the {@link Fragment} associated with a specified position in {@link #tabLayout}
         * and in the {@link #navigationView}
         */
        @Override
        public Fragment getItem(int position) {

            switch (navViewPosition) {
                case NAV_VIEW_ITEM_MOVIES:
                    if (position == 0)
                        return MediaList.newInstance(MovieType.MOVIES_POPULAR);
                    else if (position == 1)
                        return MediaList.newInstance(MovieType.MOVIES_TOP_RATED);
                    else if (position == 2)
                        return MediaList.newInstance(MovieType.MOVIES_NOW_PLAYING);
                    else if (position == 3)
                        return MediaList.newInstance(MovieType.MOVIES_UPCOMING);
                    break;

                case NAV_VIEW_ITEM_TV_SHOWS:
                    if (position == 0)
                        return MediaList.newInstance(TvShowType.TV_SERIES_POPULAR);
                    else if (position == 1)
                        return MediaList.newInstance(TvShowType.TV_SERIES_TOP_RATED);
                    else if (position == 2)
                        return MediaList.newInstance(TvShowType.TV_SERIES_AIRING);
                    else if (position == 3)
                        return MediaList.newInstance(TvShowType.TV_SERIES_ON_THE_AIR);
                    break;

                case NAV_VIEW_ITEM_PEOPLE:
                    if (position == 0)
                        return CharacterFragment.newInstance();
                    break;

                case NAV_VIEW_ITEM_CUSTOM_LIST:
                case NAV_VIEW_ITEM_FAVORITES:
                case NAV_VIEW_ITEM_WATCHLIST:
                    return null;

            }

            return null;
        }


        @Override
        public int getCount() {
            switch (navViewPosition) {
                case NAV_VIEW_ITEM_MOVIES:
                case NAV_VIEW_ITEM_TV_SHOWS:
                    return 4;
                case NAV_VIEW_ITEM_PEOPLE:
                    return 1;
            }
            return 0;
        }

        /**
         * Return a title for a specific viewpager page
         * based on actual position of {@link #navigationView}
         * and actual position of {@link #viewPager}
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (navViewPosition) {

                case NAV_VIEW_ITEM_MOVIES:
                    if (position == 0)
                        return getString(R.string.popular);
                    else if (position == 1)
                        return getString(R.string.top_rated);
                    else if (position == 2)
                        return getString(R.string.now_playing);
                    else if (position == 3)
                        return getString(R.string.upcoming);
                    break;

                case NAV_VIEW_ITEM_TV_SHOWS:
                    if (position == 0)
                        return getString(R.string.popular);
                    else if (position == 1)
                        return getString(R.string.top_rated);
                    else if (position == 2)
                        return getString(R.string.on_tv);
                    else if (position == 3)
                        return getString(R.string.airing_today);
                    break;

                case NAV_VIEW_ITEM_PEOPLE:
                    if (position == 0)
                        return getString(R.string.persons_name);
                    break;
            }

            return "";
        }


        @Override
        public int getItemPosition(@NonNull Object object) {
            if (shouldChange) {
                shouldChange = false;
                return POSITION_NONE;
            } else
                return super.getItemPosition(object);
        }

    }
}
