package com.ghalexandru.instant_movie.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.adapter.MediaAdapter;
import com.ghalexandru.instant_movie.rest.ApiClient;
import com.ghalexandru.instant_movie.rest.ApiInterface;
import com.ghalexandru.instant_movie.util.MediaType;

import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TYPE;

/**
 * Fragment used for getting a list of movies or tv shows based on a string query
 */
public class MediaSearch extends MediaFragment {

    private String query = "";
    private MediaType mediaType;

    private ApiInterface apiService;


    public static MediaSearch newInstance(MediaType mediaType) {
        MediaSearch fragment = new MediaSearch();

        Bundle args = new Bundle();
        args.putSerializable(KEY_MEDIA_TYPE, mediaType);

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

        if (getArguments() != null)
            mediaType = (MediaType) getArguments().getSerializable(KEY_MEDIA_TYPE);
    }

    /**
     * Get a list of movies or tv shows based on field {@link #query}
     */
    @Override
    protected void getMedia() {
        showProgressLoader();

        if (mediaType == MediaType.MOVIE)
            apiService.searchMovies(query, currentPage + 1, getString(R.string.api_key)).enqueue(mediaCallback);
        else if (mediaType == MediaType.TV_SHOW)
            apiService.searchTvShows(query, currentPage + 1, getString(R.string.api_key)).enqueue(mediaCallback);
    }

    @Override
    protected void setupRecyclerView() {
        layoutManager = new GridLayoutManager(getContext(), getNumberOfColumns(getContext()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mediaAdapter = new MediaAdapter(getContext(), mediaType,true);
        recyclerView.setAdapter(mediaAdapter);
        recyclerView.addOnScrollListener(recyclerCallback);
    }

    /**
     * Clear {@link #mediaAdapter} then make a server request for a list of movies or tv shows
     * based on {@param query} by calling method {@link #getMedia()}
     */
    public void search(String query) {
        this.query = query;
        mediaAdapter.clear();

        currentPage = 0;
        totalPages = 0;

        getMedia();
    }
}
