package com.ghalexandru.instant_movie.model.character;

import android.support.annotation.Nullable;

import com.ghalexandru.instant_movie.model.character.Character;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ghalexandru on 7/31/17.
 */

public   class Crew extends Character {

    @SerializedName("credit_id")
    private String creditID;

    @Nullable
    @SerializedName("gender")
    private Integer genre;

    @Nullable
    @SerializedName("job")
    private String job;

    public String getCreditID() {
        return creditID;
    }

    @Nullable
    public Integer getGenre() {
        return genre;
    }

    @Nullable
    public String getJob() {
        return job;
    }

}
