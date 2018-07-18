package com.ghalexandru.instant_movie.model.video;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class VideoResponse implements Serializable {

    @SerializedName("id")
    @Nullable
    private Integer id;

    @SerializedName("results")
    List<Video> results;

    @Nullable
    public Integer getId() {
        return id;
    }

    public List<Video> getResults() {
        return results;
    }
}
