package com.ghalexandru.instant_movie.model.media;

import com.ghalexandru.instant_movie.model.media.Media;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public  class MediaDetail extends Media {

    @SerializedName("budget")
    private  long budget;

    @SerializedName("genres")
    private List<Genres> genres;

    @SerializedName("status")
    private String status;

    public long getBudget() {
        return budget;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public String getStatus() {
        return status;
    }

    public class Genres implements Serializable{

        @SerializedName("id")
        private Integer id;

        @SerializedName("name")
        private String name;

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
