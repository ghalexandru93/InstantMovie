package com.ghalexandru.instant_movie.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.fragment.CharacterFragment;
import com.ghalexandru.instant_movie.fragment.MovieFragment;
import com.ghalexandru.instant_movie.fragment.TvShowFragment;
import com.ghalexandru.instant_movie.model.character.Crew;
import com.ghalexandru.instant_movie.model.media.Movie;
import com.ghalexandru.instant_movie.model.media.TvShow;
import com.ghalexandru.instant_movie.rest.ApiClient;
import com.ghalexandru.instant_movie.rest.ApiInterface;
import com.ghalexandru.instant_movie.util.MediaType;
import com.ghalexandru.instant_movie.util.Util;
import com.ghalexandru.instant_movie.views.BackdropView;
import com.ghalexandru.instant_movie.views.PosterImageView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_ID;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TITLE;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TYPE;

/**
 * Activity used to give additional info about a movie or a tv show
 */
public class DetailActivity extends Activity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @BindView(R.id.floating_action_menu)
    FloatingActionMenu floatingActionMenu;

    @BindView(R.id.floating_action_button_favorites)
    FloatingActionButton floatingActionButtonFavorites;

    @BindView(R.id.floating_action_button_watchlist)
    FloatingActionButton floatingActionButtonWatchlist;

    @BindView(R.id.floating_action_button_list)
    FloatingActionButton floatingActionButtonList;

    @BindView(R.id.progress_loader)
    SpinKitView progressLoader;

    @BindView(R.id.title_container)
    LinearLayout titleContainer;

    @BindView(R.id.backdrop)
    BackdropView backdropView;

    @BindView(R.id.collapsing_layout)
    CollapsingToolbarLayout collapsingLayout;

    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;

    @BindView(R.id.iv_profile_image)
    PosterImageView ivProfileImage;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private String RUNTIME_KEY_THEME_COLOR;


    /**
     * A movie or a tv show from intent
     */
    private MediaType mediaType;

    /**
     * Id for a movie or a tv show from intent
     */
    private int id;

    /**
     * viewpager adapter for {@link #viewPager}
     */
    private ViewPagerAdapter viewPagerAdapter;

    private Animation slideOutAnim;
    private Animation slideInAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        setViewsVisibility(View.INVISIBLE);

        /**
         * open shared preferences that store palette colors
         */
        sharedPref = getSharedPreferences(Util.COLOR_THEME_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        setupViews();

        /**
         * init retrofit
         */
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        if (mediaType == MediaType.MOVIE)
            apiService.getMovie(id, getString(R.string.api_key)).enqueue(new MovieCallback());
        else if (mediaType == MediaType.TV_SHOW)
            apiService.getTvShow(id, getString(R.string.api_key)).enqueue(new TvShowCallback());
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mediaType = (MediaType) getIntent().getSerializableExtra(KEY_MEDIA_TYPE);
        id = getIntent().getIntExtra(KEY_MEDIA_ID, 0);
        toolbarTitle = getIntent().getStringExtra(KEY_MEDIA_TITLE);

        /**
         * key used to store palette colors for this media
         */
        RUNTIME_KEY_THEME_COLOR = String.valueOf((toolbarTitle + id).hashCode());

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        collapsingLayout.setTitle(" ");

        appbarLayout.addOnOffsetChangedListener(offsetChangedListener);

        slideOutAnim = AnimationUtils.loadAnimation(this, R.anim.fab_slide_out_to_right);
        slideOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                floatingActionMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingActionMenu.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slideInAnim = AnimationUtils.loadAnimation(this, R.anim.fab_slide_in_from_right);
        slideInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                floatingActionMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        floatingActionButtonList.setOnClickListener(onClickListener);
        floatingActionButtonWatchlist.setOnClickListener(onClickListener);
        floatingActionButtonFavorites.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(DetailActivity.this, getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * Make some views visible or invisible
     *
     * @param visibility set this param with {@code View.INVISIBLE}
     *                   or {@code View.INVISIBLE}
     */
    private void setViewsVisibility(int visibility) {
        if (visibility == View.INVISIBLE)
            progressLoader.setVisibility(View.VISIBLE);
        else
            progressLoader.setVisibility(View.GONE);

        floatingActionMenu.setVisibility(visibility);
        appbarLayout.setVisibility(visibility);
        viewPager.setVisibility(visibility);
        tabLayout.setVisibility(visibility);
        ivProfileImage.setVisibility(visibility);
    }

    private void changeThemeColor(int mainColor) {
        appbarLayout.setBackgroundColor(mainColor);
        collapsingLayout.setContentScrimColor(mainColor);

        floatingActionMenu.setMenuButtonColorNormal(mainColor);
        floatingActionButtonFavorites.setColorPressed(mainColor);
        floatingActionButtonWatchlist.setColorPressed(mainColor);
        floatingActionButtonList.setColorPressed(mainColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(mainColor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        updateToolbarMenu();
        return true;
    }

    @Override
    protected void updateToolbarMenu() {

        Menu toolbarMenu = toolbar.getMenu();

        toolbarMenu.findItem(R.id.action_list).setVisible(false);
        toolbarMenu.findItem(R.id.action_grid).setVisible(false);
    }

    /**
     * Listener for vertical offset in {@link #appbarLayout}
     */
    private AppBarLayout.OnOffsetChangedListener offsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {

        /**
         * Field that store maximum scroll range
         */
        private int maxScrollRange = 0;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            if (maxScrollRange == 0)
                maxScrollRange = appBarLayout.getTotalScrollRange();

            /**
             * {@value offsetValue} is the ratio between current offset and {@value maxScrollRange}
             */
            float offsetValue = ((float) Math.abs(verticalOffset) / maxScrollRange);

            float alphaValue = Math.abs(offsetValue - 1f);


            changeAppBarAlpha(verticalOffset, alphaValue);
            showOrHideTitle(offsetValue);
        }

        /**
         * Change alpha value for views in {@code appbarLayout}
         */
        private void changeAppBarAlpha(int verticalOffset, float alphaValue) {
            titleContainer.setAlpha(alphaValue);
            backdropView.setAlpha(alphaValue);

            boolean maxOffset = collapsingLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingLayout);

            if (maxOffset)
                ivProfileImage.setAlpha(0f);
            else
                ivProfileImage.setAlpha(alphaValue);
        }

        /**
         * Show or hide title bar based on @param offsetValue
         */
        private void showOrHideTitle(float offsetValue) {
            String title = (String) collapsingLayout.getTitle();

            if (offsetValue >= 1f && title.equals(" ")) {
                collapsingLayout.setTitle(toolbarTitle);

                if (floatingActionMenu.getVisibility() == View.INVISIBLE)
                    floatingActionMenu.setVisibility(View.VISIBLE);
                floatingActionMenu.clearAnimation();
                floatingActionMenu.setAnimation(slideOutAnim);
                floatingActionMenu.startAnimation(slideOutAnim);

            } else if (offsetValue < 1f && !title.equals(" ")) {
                collapsingLayout.setTitle(" ");

                floatingActionMenu.clearAnimation();
                floatingActionMenu.setAnimation(slideInAnim);
                floatingActionMenu.startAnimation(slideInAnim);
            }
        }
    };


    /**
     * Retrofit callback for getting movies
     */
    private class MovieCallback implements Callback<Movie> {

        @Override
        public void onResponse(@NonNull Call<Movie> call, Response<Movie> response) {
            if (response == null || response.body() == null || response.body().getImageResponse() == null) {
                setViewsVisibility(ViewStub.VISIBLE);
                return;
            }

            /**
             * check if the color for this media exist in shared preferences
             */
            int color = sharedPref.getInt(RUNTIME_KEY_THEME_COLOR, 0);


            if (color != 0) {

                /**
                 * init the {@link #backdropView}, change color with color retrieved from shared preferences
                 * and make views visible
                 */
                backdropView.init(getSupportFragmentManager(), response.body().getImageResponse().getBackdrops(),
                        response.body().getVideoResponse(), toolbarTitle, null);
                changeThemeColor(color);
                setViewsVisibility(View.VISIBLE);
            } else {

                /**
                 * init the {@link #backdropView} and make make a request callback for images
                 */
                backdropView.init(getSupportFragmentManager(), response.body().getImageResponse().getBackdrops(),
                        response.body().getVideoResponse(), toolbarTitle, new BitmapRequestCallback());

                /**
                 * if we don't have any images in the backdrop then we let the default color and make views to be visible
                 */
                if (response.body().getImageResponse().getBackdrops().size() == 0)
                    setViewsVisibility(View.VISIBLE);

            }


            /**
             * create a list of fragments and all to the {@link #viewPagerAdapter}
             */
            List<Fragment> fragmentList = new ArrayList<>();
            fragmentList.add(MovieFragment.newInstance(response.body()));
            fragmentList.add(CharacterFragment.newInstance(response.body().getCredits().getCasts()));
            fragmentList.add(CharacterFragment.newInstance(response.body().getCredits().getCrews()));
            List<String> titleList = new ArrayList<>(Arrays.asList("OVERVIEW", "CAST", "CREW"));
            viewPagerAdapter.addFragmentList(fragmentList, titleList);


            /**
             * init the {@link PosterImageView}
             */
            ivProfileImage.init(response.body().getImageResponse().getPosters(), toolbarTitle);

            /**
             * set title of this media
             */
            tvTitle.setText(response.body().getTitle());

            List<String> directors = new ArrayList<>();

            for (Crew crew : response.body().getCredits().getCrews())
                if (crew.getJob().equals("Director"))
                    directors.add(crew.getName());

            /**
             * set main film director of this media
             */
            tvSubtitle.setText("Directed by " + Util.split(directors));
        }

        @Override
        public void onFailure(Call<Movie> call, Throwable throwable) {

        }
    }

    private class TvShowCallback implements Callback<TvShow> {

        @Override
        public void onResponse(@NonNull Call<TvShow> call, Response<TvShow> response) {
            if (response == null || response.body() == null || response.body().getImageResponse() == null) {
                setViewsVisibility(ViewStub.VISIBLE);
                return;
            }

            /**
             * init the {@link #backdropView} and make make a request callback for images
             */

            /**
             * check if the color for this media exist in shared preferences
             */
            int color = sharedPref.getInt(RUNTIME_KEY_THEME_COLOR, 0);

            if (color != 0) {
                /**
                 * init the {@link #backdropView}, change color with color retrieved from shared preferences
                 * and make views visible
                 */
                backdropView.init(getSupportFragmentManager(), response.body().getImageResponse().getBackdrops(),
                        response.body().getVideoResponse(), toolbarTitle, null);

                changeThemeColor(color);
                setViewsVisibility(View.VISIBLE);
            } else {
                /**
                 * init the {@link #backdropView} and make make a request callback for images
                 */
                backdropView.init(getSupportFragmentManager(), response.body().getImageResponse().getBackdrops(),
                        response.body().getVideoResponse(), toolbarTitle, new BitmapRequestCallback());

                /**
                 * if we don't have any images in the backdrop then we let the default color and make views to be visible
                 */
                if (response.body().getImageResponse().getBackdrops().size() == 0)
                    setViewsVisibility(View.VISIBLE);
            }


            /**
             * create a list of fragments and all to the {@link #viewPagerAdapter}
             */
            viewPagerAdapter.addFragmentList(Collections.<Fragment>singletonList(TvShowFragment.newInstance(response.body())),
                    Collections.singletonList("OVERVIEW"));

            /**
             * init the {@link PosterImageView}
             */
            ivProfileImage.init(response.body().getImageResponse().getPosters(), toolbarTitle);

            /**
             * set title of this media
             */
            tvTitle.setText(response.body().getTitle());
        }

        @Override
        public void onFailure(Call<TvShow> call, Throwable throwable) {

        }
    }

    /**
     * Listener for images in the backdrop
     */
    private class BitmapRequestCallback implements RequestListener<Bitmap> {

        /**
         * if this request failed then we let views to be visible
         */
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
            setViewsVisibility(View.VISIBLE);
            return false;
        }

        /**
         * if this request succeed then we try to extract color palettes from {@code bitmap} using {@link #paletteAsyncListener}
         */
        @Override
        public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
            Palette.Builder builder = Palette.from(bitmap);
            builder.generate(paletteAsyncListener);
            return false;
        }

        /**
         * Asynchronous listener that change theme color for this activity based on color palettes
         */
        private Palette.PaletteAsyncListener paletteAsyncListener = new Palette.PaletteAsyncListener() {

            /**
             * make all colors to have the same luminosity
             */
            private int adjustLightness(int color) {
                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                float hsl[] = new float[3];
                ColorUtils.RGBToHSL(red, green, blue, hsl);

                hsl[2] = 0.58f;

                return ColorUtils.HSLToColor(hsl);
            }

            @Override
            public void onGenerated(Palette palette) {
                if (palette != null) {

                    int defaultColor = ContextCompat.getColor(getApplicationContext(), R.color.primary);

                    int vibrantColor = palette.getVibrantColor(defaultColor);
                    int dominantColor = palette.getDominantColor(defaultColor);
                    int darkVibrantColor = palette.getDarkVibrantColor(defaultColor);
                    int darkMutedColor = palette.getDarkMutedColor(defaultColor);
                    int mutedColor = palette.getMutedColor(defaultColor);

                    int lightMutedColor = palette.getLightMutedColor(defaultColor);

                    int color;

                    if (vibrantColor != defaultColor) {
                        color = adjustLightness(vibrantColor);
                    } else if (darkVibrantColor != defaultColor)
                        color = adjustLightness(darkVibrantColor);
                    else if (dominantColor != defaultColor)
                        color = adjustLightness(dominantColor);
                    else if (darkMutedColor != defaultColor)
                        color = adjustLightness(darkMutedColor);
                    else if (mutedColor != defaultColor)
                        color = adjustLightness(mutedColor);
                    else
                        color = defaultColor;

                    changeThemeColor(color);
                    setViewsVisibility(View.VISIBLE);

                    sharedPref.edit().putInt(RUNTIME_KEY_THEME_COLOR, color).apply();
                }
            }
        };
    }

    /**
     * Viewpager adapter for this viewpager {@link #viewPager}
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList;
        private List<String> titleList;

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fragmentList = new ArrayList<>();
            titleList = new ArrayList<>();
        }

        /**
         * Add a list of fragments into this viewpager adapter at runtime
         *
         * @param fragmentList list of fragments
         * @param titleList    string list that will be used to show title of every tab in {@link #tabLayout}
         */
        public void addFragmentList(List<Fragment> fragmentList, List<String> titleList) {
            this.titleList.addAll(titleList);
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

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}
