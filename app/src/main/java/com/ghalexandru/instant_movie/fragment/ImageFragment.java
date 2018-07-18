package com.ghalexandru.instant_movie.fragment;


import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.ghalexandru.instant_movie.GlideApp;
import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.util.ImageType;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ghalexandru.instant_movie.util.Util.KEY_IMAGE_TYPE;

/**
 * A fragments used to show a image
 */
public class ImageFragment extends Fragment {

    private static final String KEY_IMAGE_PATH = "image_path";

    private String imagePath;
    private ImageType imageType;

    @BindView(R.id.image_view)
    ImageView imageView;

    @Nullable
    private View.OnClickListener onClickListener;

    @Nullable
    private RequestListener<Bitmap> requestListener;

    /**
     * Create a new instance of this fragment
     *
     * @param imagePath uri path for image
     * @param imageType type of image
     */
    public static ImageFragment newInstance(String imagePath, ImageType imageType) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(KEY_IMAGE_PATH, imagePath);
        args.putSerializable(KEY_IMAGE_TYPE, imageType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imagePath = getArguments().getString(KEY_IMAGE_PATH);
            imageType = (ImageType) getArguments().getSerializable(KEY_IMAGE_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_image, container, false);

        ButterKnife.bind(this, view);

        /**
         * if {@link #onClickListener} is not null then we will use this listener for {@link #imageView}
         */
        if (onClickListener != null)
            imageView.setOnClickListener(onClickListener);

        /**
         * get screen orientation
         */
        int orientation = getActivity().getResources().getConfiguration().orientation;

        /**
         * set {@link #imageView} scale type based on {@link #imageType} and {@link orientation}
         */
        switch (imageType) {
            case FULLSCREEN_POSTER:
                if (orientation == Configuration.ORIENTATION_PORTRAIT)
                    imageView.setScaleType(ImageView.ScaleType.FIT_END);
                else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;

            case COLLAPSING_BACKDROP:
                if (orientation == Configuration.ORIENTATION_PORTRAIT)
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;

            case FULLSCREEN_BACKDROP:
                if (orientation == Configuration.ORIENTATION_PORTRAIT)
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;

            default:
                break;
        }

        if (requestListener != null) {
            GlideApp
                    .with(imageView.getContext())
                    .asBitmap()
                    .load(imagePath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(requestListener)
                    .into(imageView);
        } else {
            GlideApp
                    .with(imageView.getContext())
                    .load(imagePath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

        return view;
    }

    public void setOnClickListener(@NonNull View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setRequestListener(@Nullable RequestListener<Bitmap> requestListener) {
        this.requestListener = requestListener;
    }
}
