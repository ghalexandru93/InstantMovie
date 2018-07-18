package com.ghalexandru.instant_movie.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;

import com.eftimoff.viewpagertransformers.ZoomOutTranformer;
import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.fragment.ImageFragment;
import com.ghalexandru.instant_movie.model.image.Image;
import com.ghalexandru.instant_movie.util.ImageType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ghalexandru.instant_movie.util.Util.KEY_IMAGE_TYPE;
import static com.ghalexandru.instant_movie.util.Util.KEY_LIST_IMAGE;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TITLE;

/**
 * Activity used to show images in {@link com.ghalexandru.instant_movie.views.BackdropView}
 * and {@link com.ghalexandru.instant_movie.views.PosterImageView}
 */
public class ImageActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();

    /**
     * List of images path that will be retrieved from intent
     */
    private List<Image> imageList;

    /**
     * Title used by the {@link #toolbar}
     */
    private String mediaTitle;

    private ImageType imageType;


    @BindView(R.id.viewpager)
    protected ViewPager viewPager;

    private final static String POSTER_URL = "http://image.tmdb.org/t/p/w342";
    private final static String BACKDROP_URL = "http://image.tmdb.org/t/p/w780";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ButterKnife.bind(this);


        imageList = (List<Image>) getIntent().getSerializableExtra(KEY_LIST_IMAGE);
        imageType = (ImageType) getIntent().getSerializableExtra(KEY_IMAGE_TYPE);
        mediaTitle = getIntent().getStringExtra(KEY_MEDIA_TITLE);


        setupViews();
    }

    @Override
    protected void updateToolbarMenu() {
        toolbar.setTitle(mediaTitle);
        toolbar.setSubtitle(viewPager.getCurrentItem() + 1 + " of " + imageList.size());


        Menu toolbarMenu = toolbar.getMenu();

        toolbarMenu.findItem(R.id.action_search).setVisible(false);
        toolbarMenu.findItem(R.id.action_list).setVisible(false);
        toolbarMenu.findItem(R.id.action_grid).setVisible(false);
    }



    @Override
    protected void setupViews() {
        super.setupViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.bringToFront();

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        /**
         * add effect to this {@link #viewPager}
         */
        viewPager.setPageTransformer(true, new ZoomOutTranformer());

        List<Fragment> fragmentList = new ArrayList<>();

        String imagePath = "";

        if (imageType == ImageType.FULLSCREEN_POSTER)
            imagePath = POSTER_URL;
        else if (imageType == ImageType.FULLSCREEN_BACKDROP)
            imagePath = BACKDROP_URL;

        /**
         * create fragments
         */
        for (Image image : imageList)
            fragmentList.add(ImageFragment.newInstance(imagePath + image.getFilePath(), imageType));

        /**
         * add all fragments to this {@link viewPagerAdapter}
         */
        viewPagerAdapter.addFragmentList(fragmentList);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                /**
                 * set current position of {@link viewPager} in the toolbar subtitle
                 */
                toolbar.setSubtitle(++position + " of " + imageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        updateToolbarMenu();

        return true;
    }

    /**
     * Viewpager adapter for {@link #viewPager} that will don't have
     * a labLayout and tab titles
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList;

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fragmentList = new ArrayList<>();
        }

        public void addFragmentList(List<Fragment> fragmentList) {
            this.fragmentList.addAll(fragmentList);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
