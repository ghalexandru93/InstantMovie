package com.ghalexandru.instant_movie.model.character;

import android.support.annotation.Nullable;

import com.ghalexandru.instant_movie.model.character.Character;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ghalexandru on 7/31/17.
 */

public  class Cast extends Character {

    @SerializedName("cast_id")
    private Integer castID;

    @SerializedName("credit_id")
    private String creditID;

    @SerializedName("character")
    private String character;

    @Nullable
    @SerializedName("gender")
    private Integer genre;

    @SerializedName("order")
    private Integer order;

    public Integer getCastID() {
        return castID;
    }

    public String getCreditID() {
        return creditID;
    }

    public String getCharacter() {
        return character;
    }

    @Nullable
    public Integer getGenre() {
        return genre;
    }


    public Integer getOrder() {
        return order;
    }

}
