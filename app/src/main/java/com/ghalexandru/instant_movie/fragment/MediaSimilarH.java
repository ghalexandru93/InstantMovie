package com.ghalexandru.instant_movie.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.adapter.MediaAdapter;
import com.ghalexandru.instant_movie.model.media.MediaResponse;
import com.ghalexandru.instant_movie.util.MediaType;

/**
 * Fragment used to show a list of movies or tv shows on horizontal
 */
public class MediaSimilarH extends MediaFragment {

    private static final String KEY_MEDIA_SIMILAR = "media_similar";
    private MediaResponse mediaResponse;

    private static final String TAG = MediaSimilarH.class.getSimpleName();

    /**
     * Create a new instance of this fragment
     */
    public static MediaSimilarH newInstance(MediaResponse mediaResponse) {
        MediaSimilarH fragment = new MediaSimilarH();
        Bundle args = new Bundle();

        args.putSerializable(KEY_MEDIA_SIMILAR, mediaResponse);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            mediaResponse = (MediaResponse) getArguments().getSerializable(KEY_MEDIA_SIMILAR);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) getContext().getResources().getDimension(R.dimen.grid_poster_height)));

        hideProgressLoader();

        return view;
    }

    @Override
    protected void setupSpaceDecoration() {
        float spaceDecorationValue = getResources().getDimension(R.dimen.linear_space_decoration);
        itemDecoration = new ItemDecoration((int) spaceDecorationValue);
    }

    @Override
    protected void updateItemDecoration() {
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected void getMedia() {
        mediaAdapter.add(mediaResponse.getResults());
    }

    @Override
    protected void setupRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);

        mediaAdapter = new MediaAdapter(getContext(), MediaType.MOVIE, false);
        recyclerView.setAdapter(mediaAdapter);
    }
}
