package com.ghalexandru.instant_movie.model.character;

import com.ghalexandru.instant_movie.model.character.Character;
import com.ghalexandru.instant_movie.model.media.Media;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ghalexandru on 5/15/17.
 */

public  class Person extends Character {

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("known_for")
    private List<Media> knownFor;

    public boolean isAdult() {
        return adult;
    }

    public Double getPopularity() {
        return popularity;
    }

    public List<Media> getKnownFor() {
        return knownFor;
    }
}
