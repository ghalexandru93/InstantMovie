package com.ghalexandru.instant_movie.model.image;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Image implements Serializable {

    @SerializedName("aspect_ratio")
    private Double aspectRatio;

    @SerializedName("file_path")
    private String filePath;

    @SerializedName("height")
    private Integer height;

    @SerializedName("iso_639_1")
    private String iso;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("vote_count")
    private Integer voteCount;

    @SerializedName("width")
    private Integer width;

    public Double getAspectRatio() {
        return aspectRatio;
    }

    public String getFilePath() {
        return filePath;
    }

    public Integer getHeight() {
        return height;
    }

    public String getIso() {
        return iso;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Integer getWidth() {
        return width;
    }
}
