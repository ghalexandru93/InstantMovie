package com.ghalexandru.instant_movie.fragment;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.adapter.MediaAdapter;
import com.ghalexandru.instant_movie.rest.ApiClient;
import com.ghalexandru.instant_movie.rest.ApiInterface;
import com.ghalexandru.instant_movie.util.MediaType;

import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_ID;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TYPE;

/**
 * Fragment used to show a list of movies or tv shows on vertical
 */
public class MediaSimilarV extends MediaFragment {

    private MediaType mediaType;
    private int id;

    private ApiInterface apiService;


    /**
     * Create a new instance of this fragment
     * @param mediaType - a movie or a tv show
     * @param id - tmdb id for this movie or tv show
     */
    public static MediaSimilarV newInstance(MediaType mediaType, int id) {
        MediaSimilarV fragment = new MediaSimilarV();

        Bundle args = new Bundle();
        args.putSerializable(KEY_MEDIA_TYPE, mediaType);
        args.putInt(KEY_MEDIA_ID, id);

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

        if (getArguments() != null) {
            mediaType = (MediaType) getArguments().getSerializable(KEY_MEDIA_TYPE);
            id = getArguments().getInt(KEY_MEDIA_ID);
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

    @Override
    protected void getMedia() {
        showProgressLoader();

        if (mediaType == MediaType.MOVIE)
            apiService.getSimilarMovies(id, getString(R.string.api_key), currentPage + 1).enqueue(mediaCallback);
        else if (mediaType == MediaType.TV_SHOW)
            apiService.getSimilarTvShows(id, getString(R.string.api_key), currentPage + 1).enqueue(mediaCallback);

    }
}
