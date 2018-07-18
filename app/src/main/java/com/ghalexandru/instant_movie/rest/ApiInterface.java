package com.ghalexandru.instant_movie.rest;

import com.ghalexandru.instant_movie.model.media.MediaResponse;
import com.ghalexandru.instant_movie.model.media.Movie;
import com.ghalexandru.instant_movie.model.character.PersonResponse;
import com.ghalexandru.instant_movie.model.media.TvShow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Api interface used by {@link retrofit2.Retrofit}
 */
public interface ApiInterface {

    String ID = "id";
    String API_KEY = "api_key";
    String PAGE = "page";

    String MOVIE = "movie";
    String TV = "tv";

    String UPCOMING = "upcoming";
    String NOW_PLAYING = "now_playing";
    String TOP_RATED = "top_rated";
    String POPULAR = "popular";
    String AIRING_TODAY = "airing_today";
    String ON_THE_AIR = "on_the_air";

    String SIMILAR = "similar";
    String SEARCH = "search";

    String APPEND_TO_RESPOND = "?append_to_response=videos,images,credits,similar";

    @GET(MOVIE + "/{id}" + APPEND_TO_RESPOND)
    Call<Movie> getMovie(@Path(ID) int id, @Query(API_KEY) String apiKey);

    @GET(TV + "/{id}" + APPEND_TO_RESPOND)
    Call<TvShow> getTvShow(@Path(ID) int id, @Query(API_KEY) String apiKey);

    @GET(MOVIE + "/" + UPCOMING)
    Call<MediaResponse> getUpcomingMovies(@Query(API_KEY) String apiKey, @Query(PAGE) int page);

    @GET(MOVIE + "/" + NOW_PLAYING)
    Call<MediaResponse> getNowPlayingMovies(@Query(API_KEY) String apiKey, @Query(PAGE) int page);

    @GET(MOVIE + "/" + TOP_RATED)
    Call<MediaResponse> getTopRatedMovies(@Query(API_KEY) String apiKey, @Query(PAGE) int page);

    @GET(MOVIE + "/" + POPULAR)
    Call<MediaResponse> getPopularMovies(@Query(API_KEY) String apiKey, @Query(PAGE) int page);

    @GET(TV + "/" + POPULAR)
    Call<MediaResponse> getPopularTvShows(@Query(API_KEY) String apiKey, @Query(PAGE) int page);

    @GET(TV + "/" + TOP_RATED)
    Call<MediaResponse> getTopRatedTvShows(@Query(API_KEY) String apiKey, @Query(PAGE) int page);

    @GET(TV + "/" + AIRING_TODAY)
    Call<MediaResponse> getAiringTvShows(@Query(API_KEY) String apiKey, @Query(PAGE) int page);

    @GET(TV + "/" + ON_THE_AIR)
    Call<MediaResponse> getOnTheAirTvShows(@Query(API_KEY) String apiKey, @Query(PAGE) int page);


    @GET(SEARCH + "/" + TV)
    Call<MediaResponse> searchTvShows(@Query("query") String query, @Query(PAGE) int page, @Query(API_KEY) String apiKey);

    @GET(SEARCH + "/" + MOVIE)
    Call<MediaResponse> searchMovies(@Query("query") String query, @Query(PAGE) int page, @Query(API_KEY) String apiKey);


    @GET("person/popular")
    Call<PersonResponse> getPopularPersons(@Query(API_KEY) String apiKey, @Query(PAGE) int page);

    @GET(MOVIE + "/{id}/" + SIMILAR)
    Call<MediaResponse> getSimilarMovies(@Path(ID) int id, @Query(API_KEY) String apiKey, @Query(PAGE) int page);

    @GET(TV + "/{id}/" + SIMILAR)
    Call<MediaResponse> getSimilarTvShows(@Path(ID) int id, @Query(API_KEY) String apiKey, @Query(PAGE) int page);


    @GET("discover/" + MOVIE)
    Call<MediaResponse> discoverMovies(@Query(API_KEY) String apiKey, @Query("language") String language, @Query("sort_by") String sort, @Query(PAGE) int page);
}
