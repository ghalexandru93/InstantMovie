package com.ghalexandru.instant_movie.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.model.media.MediaDetail;
import com.ghalexandru.instant_movie.model.media.TvShow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ghalexandru.instant_movie.util.Util.getAString;
import static com.ghalexandru.instant_movie.util.Util.split;

/**
 * Fragment used to show detailed info about a tv show
 */
public class TvShowFragment extends Fragment {

    @BindView(R.id.tv_overview)
    TextView tvOverview;

    @BindView(R.id.tv_genres)
    TextView tvGenres;

    @BindView(R.id.tv_rating)
    TextView tvRating;

    @BindView(R.id.tv_vote_count)
    TextView tvVoteCount;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    private static final String KEY_SHOW_OVERVIEW = "show_overview";
    private TvShow tvShow;

    /**
     * Create a new instance of this fragment
     *
     * @param tvShow a {@link TvShow} model that will be used
     *              to fill views in this fragment with info
     */
    public static TvShowFragment newInstance(TvShow tvShow) {
        TvShowFragment fragment = new TvShowFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_SHOW_OVERVIEW, tvShow);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            tvShow = (TvShow) getArguments().getSerializable(KEY_SHOW_OVERVIEW);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);

        ButterKnife.bind(this, view);
        setupViews(tvShow);

        return view;
    }

    protected void setupViews(MediaDetail mediaDetail) {

        TvShow show = (TvShow) mediaDetail;

        tvOverview.setText(getAString(show.getOverview()));

        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < show.getGenres().size(); i++)
            stringList.add(show.getGenres().get(i).getName());

        tvGenres.setText(split(stringList));

        tvRating.setText(getAString(show.getVoteAverage()));
        tvVoteCount.setText(" (" + getAString(show.getVoteCount()) + " votes)");
        tvStatus.setText(getAString(show.getStatus()));
    }

}
