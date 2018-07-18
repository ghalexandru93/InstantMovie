package com.ghalexandru.instant_movie.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.adapter.MediaAdapter;
import com.ghalexandru.instant_movie.model.media.Media;
import com.ghalexandru.instant_movie.model.media.MediaResponse;
import com.ghalexandru.instant_movie.rest.ApiClient;
import com.ghalexandru.instant_movie.rest.ApiInterface;
import com.ghalexandru.instant_movie.util.MediaType;
import com.ghalexandru.instant_movie.util.MovieType;
import com.ghalexandru.instant_movie.util.TvShowType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TYPE;
import static com.ghalexandru.instant_movie.util.Util.KEY_MOVIE_TYPE;
import static com.ghalexandru.instant_movie.util.Util.KEY_TV_SHOW_TYPE;

/**
 * Fragment used for showing a list of movies or tv shows
 */
public class MediaList extends MediaFragment {

    private MediaType mediaType;

    @Nullable
    private MovieType movieType;

    @Nullable
    private TvShowType tvShowType;

    private ApiInterface apiService;

    private String TAG = getClass().getSimpleName();

    /**
     * Constructor used for populate {@link #recyclerView} with a list of movies
     *
     * @param movieType the type of movies to populate {@link #recyclerView}
     */
    public static MediaList newInstance(@NonNull MovieType movieType) {
        MediaList fragment = new MediaList();


        Bundle args = new Bundle();
        args.putSerializable(KEY_MEDIA_TYPE, MediaType.MOVIE);
        args.putSerializable(KEY_MOVIE_TYPE, movieType);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Constructor used for populate {@link #recyclerView} with a list of tv shows
     *
     * @param tvShowType the type of tv shows to populate {@link #recyclerView}
     */
    public static MediaList newInstance(@NonNull TvShowType tvShowType) {
        MediaList fragment = new MediaList();

        Bundle args = new Bundle();
        args.putSerializable(KEY_MEDIA_TYPE, MediaType.TV_SHOW);
        args.putSerializable(KEY_TV_SHOW_TYPE, tvShowType);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * init retrofit
         */
        apiService = ApiClient.getClient().create(ApiInterface.class);

        /**
         * retrieve saved bundle values
         */
        if (getArguments() != null) {
            mediaType = (MediaType) getArguments().getSerializable(KEY_MEDIA_TYPE);
            movieType = (MovieType) getArguments().getSerializable(KEY_MOVIE_TYPE);
            tvShowType = (TvShowType) getArguments().getSerializable(KEY_TV_SHOW_TYPE);
        }
    }

    @Override
    protected void getMedia() {
        showProgressLoader();

        if (mediaType == MediaType.MOVIE) {
            switch (movieType) {
                case MOVIES_POPULAR:
                    apiService.getPopularMovies(getString(R.string.api_key), currentPage + 1).enqueue(mediaCallback);
                    break;
                case MOVIES_TOP_RATED:
                    apiService.getTopRatedMovies(getString(R.string.api_key), currentPage + 1).enqueue(mediaCallback);
                    break;
                case MOVIES_UPCOMING:
                    apiService.getUpcomingMovies(getString(R.string.api_key), currentPage + 1).enqueue(mediaCallback);
                    break;
                case MOVIES_NOW_PLAYING:
                    apiService.getNowPlayingMovies(getString(R.string.api_key), currentPage + 1).enqueue(mediaCallback);
                    break;
            }
        } else if (mediaType == MediaType.TV_SHOW) {
            switch (tvShowType) {
                case TV_SERIES_POPULAR:
                    apiService.getPopularTvShows(getString(R.string.api_key), currentPage + 1).enqueue(mediaCallback);
                    break;

                case TV_SERIES_TOP_RATED:
                    apiService.getTopRatedTvShows(getString(R.string.api_key), currentPage + 1).enqueue(mediaCallback);
                    break;

                case TV_SERIES_AIRING:
                    apiService.getAiringTvShows(getString(R.string.api_key), currentPage + 1).enqueue(mediaCallback);
                    break;

                case TV_SERIES_ON_THE_AIR:
                    apiService.getOnTheAirTvShows(getString(R.string.api_key), currentPage + 1).enqueue(mediaCallback);
                    break;
            }
        }
    }

    @Override
    protected void setupRecyclerView() {
        layoutManager = new GridLayoutManager(getContext(), getNumberOfColumns(getContext()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mediaAdapter = new MediaAdapter(getContext(), mediaType, true);
        recyclerView.setAdapter(mediaAdapter);
        recyclerView.addOnScrollListener(recyclerCallback);
    }


}
