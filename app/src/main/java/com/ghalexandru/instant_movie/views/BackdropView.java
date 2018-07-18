package com.ghalexandru.instant_movie.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestListener;
import com.eftimoff.viewpagertransformers.StackTransformer;
import com.ghalexandru.instant_movie.activity.ImageActivity;
import com.ghalexandru.instant_movie.fragment.ImageFragment;
import com.ghalexandru.instant_movie.fragment.VideoFragment;
import com.ghalexandru.instant_movie.model.image.Image;
import com.ghalexandru.instant_movie.model.video.Video;
import com.ghalexandru.instant_movie.model.video.VideoResponse;
import com.ghalexandru.instant_movie.util.ImageType;
import com.ghalexandru.instant_movie.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ghalexandru.instant_movie.util.Util.KEY_IMAGE_TYPE;
import static com.ghalexandru.instant_movie.util.Util.KEY_LIST_IMAGE;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TITLE;

/**
 * A {@link ViewPager} with a {@link ViewPagerAdapter} that will show images
 */
public class BackdropView extends ViewPager {

    private final static String TAG = BackdropView.class.getSimpleName();

    /**
     * Number of videos to be show
     */
    private final static int NUM_OF_VIDEO = 3;

    /**
     * URL for backdrops
     */
    private final static String BACKDROP_URL = "http://image.tmdb.org/t/p/w780";

    private List<Image> backdropList;
    private String mediaTitle;

    public BackdropView(Context context) {
        super(context);
    }

    public BackdropView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Init this view
     *
     * @param backdropList    a list of image which will be shown
     * @param mediaTitle      title that will be shown
     * @param requestListener a bitmap listener
     */
    public void init(FragmentManager fragmentManager, List<Image> backdropList,
                     VideoResponse videoResponse, String mediaTitle,
                     final @Nullable RequestListener<Bitmap> requestListener) {
        this.mediaTitle = mediaTitle;
        if (getWindowToken() == null) return;

        this.backdropList = backdropList;


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager);
        setAdapter(viewPagerAdapter);

        /**
         * set #viewpager effect
         */
        setPageTransformer(true, new StackTransformer());

        /**
         * get a list of video trailers for a specific movie or tv show
         */
        List<Video> videoList = new ArrayList<>();
        if (videoResponse != null) {
            for (Video video : videoResponse.getResults())
                if (video.getType().equals("Trailer"))
                    videoList.add(video);
        }

        String imagePath, videoPath;
        List<Fragment> fragmentList = new ArrayList<>();


        /**
         * #fragmentList will be filled with {@link #NUM_OF_VIDEO} of #VideoFragment
         * and the rest is #ImageFragment
         */
        for (int i = 0; i < backdropList.size(); i++) {

            if (i < NUM_OF_VIDEO && videoList.size() > i) {
                imagePath = BACKDROP_URL + backdropList.get(i).getFilePath();
                videoPath = Util.YOUTUBE_URL_PATH + videoList.get(i).getKey();
                fragmentList.add(VideoFragment.newInstance(imagePath, videoPath));

            } else {
                imagePath = backdropList.get(i).getFilePath();
                fragmentList.add(ImageFragment.newInstance(BACKDROP_URL + imagePath, ImageType.COLLAPSING_BACKDROP));
                ((ImageFragment) fragmentList.get(i)).setOnClickListener(onClickListener);
            }
        }

        if (fragmentList.size() > 0 && fragmentList.get(0) != null && requestListener != null) {
            if (fragmentList.get(0) instanceof VideoFragment)
                ((VideoFragment) fragmentList.get(0)).setRequestListener(requestListener);
            else if (fragmentList.get(0) instanceof ImageFragment)
                ((ImageFragment) fragmentList.get(0)).setRequestListener(requestListener);
        }

        viewPagerAdapter.addFragmentList(fragmentList);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ImageActivity.class);
            intent.putExtra(KEY_LIST_IMAGE, (Serializable) backdropList);
            intent.putExtra(KEY_IMAGE_TYPE, ImageType.FULLSCREEN_BACKDROP);
            intent.putExtra(KEY_MEDIA_TITLE, mediaTitle);
            getContext().startActivity(intent);
        }
    };

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
