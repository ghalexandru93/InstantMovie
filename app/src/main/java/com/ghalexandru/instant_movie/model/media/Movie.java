package com.ghalexandru.instant_movie.model.media;

import com.ghalexandru.instant_movie.model.character.Credits;
import com.ghalexandru.instant_movie.model.image.ImageResponse;
import com.ghalexandru.instant_movie.model.video.VideoResponse;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public  class Movie extends MediaDetail {

    @SerializedName("images")
    private ImageResponse imageResponse;

    @SerializedName("videos")
    private VideoResponse videoResponse;

    @SerializedName("credits")
    private Credits credits;

    @SerializedName("similar")
    private MediaResponse similarMovies;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("revenue")
    private long revenue;

    @SerializedName("runtime")
    private int runtime;

    public ImageResponse getImageResponse() {
        return imageResponse;
    }

    public VideoResponse getVideoResponse() {
        return videoResponse;
    }

    public String getTagline() {
        return tagline;
    }

    public Credits getCredits() {
        return credits;
    }

    public MediaResponse getSimilarMovies() {
        return similarMovies;
    }

    @SerializedName("spoken_languages")
    private List<SpokenLanguages> spokenLanguages;

    public long getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public List<SpokenLanguages> getSpokenLanguages() {
        return spokenLanguages;
    }

    public class SpokenLanguages implements Serializable {

        @SerializedName("iso_639_1")
        private String iso;

        @SerializedName("name")
        private String name;

        public String getIso() {
            return iso;
        }

        public String getName() {
            return name;
        }
    }

}
