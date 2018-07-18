package com.ghalexandru.instant_movie.model.character;

import com.ghalexandru.instant_movie.model.character.Person;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ghalexandru on 5/15/17.
 */

public class PersonResponse {

    @SerializedName("results")
    private List<Person> results;

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

    public List<Person> getResults() {
        return results;
    }
}
