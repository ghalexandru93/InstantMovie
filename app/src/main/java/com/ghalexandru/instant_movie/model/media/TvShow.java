package com.ghalexandru.instant_movie.model.media;

import com.ghalexandru.instant_movie.model.image.ImageResponse;
import com.ghalexandru.instant_movie.model.video.VideoResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ghalexandru on 6/25/17.
 */

public  class TvShow extends MediaDetail {

    @SerializedName("last_air_date")
    private String lastAirDate;

    @SerializedName("episode_run_time")
    private List<Integer> episodeRunTime;

    @SerializedName("number_of_episodes")
    private int numberOfEpisodes;

    @SerializedName("number_of_seasons")
    private int numberOfSeasons;

    public List<Integer> getEpisodeRunTime() {
        return episodeRunTime;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    @SerializedName("images")
    private ImageResponse imageResponse;

    @SerializedName("videos")
    private VideoResponse videoResponse;

    public ImageResponse getImageResponse() {
        return imageResponse;
    }

    public VideoResponse getVideoResponse() {
        return videoResponse;
    }

}
