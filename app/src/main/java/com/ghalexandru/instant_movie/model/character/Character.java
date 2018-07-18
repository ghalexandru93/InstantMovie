package com.ghalexandru.instant_movie.model.character;

import android.support.annotation.Nullable;

import com.ghalexandru.instant_movie.model.Model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ghalexandru on 11/11/2017.
 */

public class Character extends Model implements Serializable {

    @SerializedName("id")
    private Integer id;

    @Nullable
    @SerializedName("profile_path")
    private String profilePath;

    @SerializedName("name")
    private String name;

    public Integer getId() {
        return id;
    }

    @Nullable
    public String getProfilePath() {
        return profilePath;
    }

    public String getName() {
        return name;
    }
}
