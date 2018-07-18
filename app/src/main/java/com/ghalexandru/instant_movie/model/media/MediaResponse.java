package com.ghalexandru.instant_movie.model.media;

import com.ghalexandru.instant_movie.model.media.Media;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MediaResponse implements Serializable {

    @SerializedName("results")
    private List<Media> results;

    @SerializedName("page")
    private int page;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Media> getResults() {
        return results;
    }
}
