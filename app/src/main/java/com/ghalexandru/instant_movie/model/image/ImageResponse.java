package com.ghalexandru.instant_movie.model.image;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public  class ImageResponse implements Serializable {

    @SerializedName("id")
    @Nullable
    private Integer id;

    @SerializedName("backdrops")
    private List<Image> backdrops;

    @SerializedName("posters")
    private List<Image> posters;

    @Nullable
    public Integer getId() {
        return id;
    }

    public List<Image> getBackdrops() {
        return backdrops;
    }

    @Nullable
    public List<Image> getPosters() {
        return posters;
    }
}
