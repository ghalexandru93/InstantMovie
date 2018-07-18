package com.ghalexandru.instant_movie.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ghalexandru.instant_movie.GlideApp;
import com.ghalexandru.instant_movie.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Fragment used to show a video
 */
public class VideoFragment extends Fragment {

    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_VIDEO_PATH = "video_path";

    @BindView(R.id.iv_video)
    ImageView ivVideo;

    private String imagePath;
    private String videoPath;

    private RequestListener<Bitmap> requestListener;

    /**
     * Create a new instance of this fragment
     *
     * @param imagePath image that will be showing
     * @param videoPath URL path for video
     */
    public static VideoFragment newInstance(String imagePath, String videoPath) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(KEY_IMAGE_PATH, imagePath);
        args.putString(KEY_VIDEO_PATH, videoPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imagePath = getArguments().getString(KEY_IMAGE_PATH);
            videoPath = getArguments().getString(KEY_VIDEO_PATH);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_video, container, false);

        ButterKnife.bind(this, view);

        /**
         * if {@link #requestListener} is set then we download image as bitmap and add him to {@link #ivVideo}
         */
        if(requestListener != null){
            GlideApp
                    .with(ivVideo.getContext())
                    .asBitmap()
                    .load(imagePath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(requestListener)
                    .into(ivVideo);
        }
        else{
            GlideApp
                    .with(ivVideo.getContext())
                    .load(imagePath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivVideo);
        }

        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoPath)));
            }
        });

        return view;
    }

    /**
     * Add listener for {@link #ivVideo}
     * @param requestListener a bitmap listener that will notify if {@link #ivVideo} is ready
     */
    public void setRequestListener(RequestListener<Bitmap> requestListener) {
        this.requestListener = requestListener;
    }
}
