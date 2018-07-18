package com.ghalexandru.instant_movie.model.character;

import com.ghalexandru.instant_movie.model.character.Cast;
import com.ghalexandru.instant_movie.model.character.Crew;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ghalexandru on 7/31/17.
 */

public  class Credits implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("cast")
    private List<Cast> casts;

    @SerializedName("crew")
    private List<Crew> crews;

    public Integer getId() {
        return id;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public List<Crew> getCrews() {
        return crews;
    }
}
