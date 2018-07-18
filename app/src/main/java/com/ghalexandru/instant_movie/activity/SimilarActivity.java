package com.ghalexandru.instant_movie.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.fragment.MediaSimilarV;
import com.ghalexandru.instant_movie.util.MediaType;
import com.ghalexandru.instant_movie.util.Util;

import butterknife.ButterKnife;

import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_ID;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TITLE;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TYPE;

/**
 * Activity used to show similar movies or tv shows
 */
public class SimilarActivity extends Activity {

    private MediaType mediaType;
    private String mediaTitle;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_similar);

        ButterKnife.bind(this);
        setupViews();

        /**
         * replace {@code R.id.fragment_container} with a list of movies or tv shows
         */
        replaceFragment(R.id.fragment_container, MediaSimilarV.newInstance(mediaType, id));
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mediaType = (MediaType) getIntent().getSerializableExtra(KEY_MEDIA_TYPE);
        id = getIntent().getIntExtra(KEY_MEDIA_ID, 0);
        mediaTitle = getIntent().getStringExtra(KEY_MEDIA_TITLE);
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

        toolbar.setTitle("Similar Movies");
        toolbar.setSubtitle(mediaTitle);
        Menu toolbarMenu = toolbar.getMenu();

        toolbarMenu.findItem(R.id.action_list).setVisible(viewMode == Util.RECYCLE_VIEW_GRID);
        toolbarMenu.findItem(R.id.action_grid).setVisible(viewMode == Util.RECYCLE_VIEW_LIST);
    }

    /**
     * Method that will replace a {@link android.widget.FrameLayout} with a {@link Fragment}
     */
    private void replaceFragment(int id, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(id, fragment)
                .commit();
    }
}
