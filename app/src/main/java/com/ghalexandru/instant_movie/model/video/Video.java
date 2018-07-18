package com.ghalexandru.instant_movie.model.video;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public  class Video implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("iso_639_1")
    private String iso639;

    @SerializedName("iso_3166_1")
    private String iso_3166;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private Integer size;

    @SerializedName("type")
    private String type;

    public String getId() {
        return id;
    }

    public String getIso639() {
        return iso639;
    }

    public String getIso_3166() {
        return iso_3166;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public Integer getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
