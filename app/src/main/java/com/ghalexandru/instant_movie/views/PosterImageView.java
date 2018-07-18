package com.ghalexandru.instant_movie.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

import com.ghalexandru.instant_movie.GlideApp;
import com.ghalexandru.instant_movie.activity.ImageActivity;
import com.ghalexandru.instant_movie.model.image.Image;
import com.ghalexandru.instant_movie.util.ImageType;
import com.ghalexandru.instant_movie.util.Util;

import java.io.Serializable;
import java.util.List;

import static com.ghalexandru.instant_movie.util.Util.KEY_IMAGE_TYPE;
import static com.ghalexandru.instant_movie.util.Util.KEY_LIST_IMAGE;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TITLE;


/**
 * An {@link android.widget.ImageView} used for showing poster for movies or tv shows
 */
public class PosterImageView extends android.support.v7.widget.AppCompatImageView {


    public PosterImageView(Context context) {
        super(context);
    }

    public PosterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PosterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(final List<Image> posterImages, final String mediaTitle) {

        if (getWindowToken() == null || posterImages == null || posterImages.size() < 1) return;


        GlideApp
                .with(getContext())
                .load(Util.IMAGE_URL_PATH + posterImages.get(0).getFilePath())
                .into(this);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImageActivity.class);
                intent.putExtra(KEY_LIST_IMAGE, (Serializable) posterImages);
                intent.putExtra(KEY_IMAGE_TYPE, ImageType.FULLSCREEN_POSTER);
                intent.putExtra(KEY_MEDIA_TITLE, mediaTitle);
                getContext().startActivity(intent);
            }
        });
    }
}
