package com.ghalexandru.instant_movie.model.media;

import android.support.annotation.Nullable;

import com.ghalexandru.instant_movie.model.Model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class Media extends Model implements Serializable {


    @SerializedName("id")
    private int id;

    @SerializedName(value = "title", alternate = "name")
    private String title;

    @SerializedName(value = "release_date", alternate = "first_air_date")
    private String releaseDate;

    @Nullable
    @SerializedName("poster_path")
    private String posterPath;

    @Nullable
    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private long voteCount;

    @Nullable
    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    @Nullable
    public String getMediaType() {
        return mediaType;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() {
        return id;
    }

    public long getVoteCount() {
        return voteCount;
    }

    @Nullable
    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }
}
