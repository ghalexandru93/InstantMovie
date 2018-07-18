package com.ghalexandru.instant_movie.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.fragment.MediaSearch;
import com.ghalexandru.instant_movie.rest.ApiInterface;
import com.ghalexandru.instant_movie.util.MediaType;
import com.ghalexandru.instant_movie.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Activity used to search movies or tv shows
 */
public class SearchActivity extends Activity {

    /**
     * Adapter for {@link #viewPager}
     */
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);
        setupViews();
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        List<Fragment> fragmentList = Arrays.<Fragment>asList(
                MediaSearch.newInstance(MediaType.MOVIE),
                MediaSearch.newInstance(MediaType.TV_SHOW)
        );
        viewPagerAdapter.setFragments(fragmentList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();

        /**
         * inflate menu {@code R.menu.search_view_item}
         */
        inflater.inflate(R.menu.search_view_item, menu);
        MenuItem searchViewItem = menu.findItem(R.id.search_view);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewItem.expandActionView();
        searchView.setOnQueryTextListener(onQueryTextListener);

        updateToolbarMenu();

        return true;
    }

    @Override
    protected void updateToolbarMenu() {
        int viewMode = sharedPref.getInt(Util.RECYCLE_VIEW_MODE, Util.RECYCLE_VIEW_LIST);

        toolbar.setTitle("");
        Menu toolbarMenu = toolbar.getMenu();

        toolbarMenu.findItem(R.id.action_search).setVisible(false);

        toolbarMenu.findItem(R.id.action_list).setVisible(viewMode == Util.RECYCLE_VIEW_GRID);
        toolbarMenu.findItem(R.id.action_grid).setVisible(viewMode == Util.RECYCLE_VIEW_LIST);
    }

    /**
     * ViewPager adapter used for this activity
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<String> titles;
        private List<Fragment> fragments;

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fragments = new ArrayList<>();
            titles = Arrays.asList(getString(R.string.movies_name), getString(R.string.shows_name));
        }

        void setFragments(List<Fragment> fragments) {
            this.fragments = new ArrayList<>(fragments);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        /**
         * Fill current fragment with a list of movies or tv show
         * based on string query
         */
        @Override
        public boolean onQueryTextChange(String newText) {

            MediaSearch mediaSearch = (MediaSearch) viewPagerAdapter.getItem(viewPager.getCurrentItem());
            mediaSearch.search(newText);

            return false;
        }
    };

}
