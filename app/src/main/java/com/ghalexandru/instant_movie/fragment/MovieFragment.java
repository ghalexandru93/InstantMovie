package com.ghalexandru.instant_movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.activity.SimilarActivity;
import com.ghalexandru.instant_movie.model.media.Movie;
import com.ghalexandru.instant_movie.util.MediaType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_ID;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TITLE;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TYPE;
import static com.ghalexandru.instant_movie.util.Util.convertDate;
import static com.ghalexandru.instant_movie.util.Util.getAString;
import static com.ghalexandru.instant_movie.util.Util.split;


/**
 * Fragment used to show detailed info about a movie
 */
public class MovieFragment extends Fragment {

    @BindView(R.id.tv_overview)
    TextView tvOverview;

    @BindView(R.id.tv_genres)
    TextView tvGenres;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;

    @BindView(R.id.tv_duration)
    TextView tvDuration;

    @BindView(R.id.tv_rating)
    TextView tvRating;

    @BindView(R.id.tv_vote_count)
    TextView tvVoteCount;

    @BindView(R.id.similar_movies)
    LinearLayout similarMovies;

    @BindView(R.id.tv_view_all)
    TextView tvViewAll;

    private static final String KEY_MOVIE_OVERVIEW = "fragment_movie";
    private Movie movie;

    /**
     * Create a new instance of this fragment
     *
     * @param movie a {@link Movie} model that will be used
     *              to fill views in this fragment with info
     */
    public static MovieFragment newInstance(Movie movie) {
        MovieFragment fragment = new MovieFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_MOVIE_OVERVIEW, movie);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            movie = (Movie) getArguments().getSerializable(KEY_MOVIE_OVERVIEW);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * inflate layout {@link R.layout.fragment_movie}
         */
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        ButterKnife.bind(this, view);
        setupViews();

        /**
         * if {@link #movie} contains similar movies then we replace {@link R.id.similar_fragment} with a new instance of {@link MediaSimilarH},
         * else we simply we make {@link #similarMovies} invisible
         */
        if (movie.getSimilarMovies() == null || movie.getSimilarMovies().getResults() == null || movie.getSimilarMovies().getResults().size() == 0)
            similarMovies.setVisibility(View.GONE);
        else
            replaceFragment(R.id.similar_fragment, MediaSimilarH.newInstance(movie.getSimilarMovies()));

        return view;
    }

    /**
     * Method used to replace a {@link android.widget.FrameLayout} with a fragment
     *
     * @param id       id of {@link android.widget.FrameLayout}
     * @param fragment fragment that will replace that {@link android.widget.FrameLayout}
     */
    private void replaceFragment(int id, Fragment fragment) {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(id, fragment)
                .commit();
    }

    /**
     * Fill fragment views with info
     */
    private void setupViews() {
        tvOverview.setText(getAString(movie.getOverview()));

        String duration = (movie.getRuntime() / 60) + " hour and " + (movie.getRuntime() % 60) + " minutes";
        duration = getAString(duration);

        tvDuration.setText(duration);

        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < movie.getGenres().size(); i++)
            stringList.add(movie.getGenres().get(i).getName());

        tvStatus.setText(getAString(movie.getStatus()));
        tvGenres.setText(split(stringList));
        tvReleaseDate.setText(convertDate(movie.getReleaseDate()));
        tvRating.setText(getAString(movie.getVoteAverage()));
        tvVoteCount.setText(" (" + getAString(movie.getVoteCount()) + " votes)");

        tvViewAll.setOnClickListener(new ViewSimilarMovies());
    }

    /**
     * A listener that will start {@link SimilarActivity} if method onClick is triggered
     */
    private class ViewSimilarMovies implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), SimilarActivity.class);
            intent.putExtra(KEY_MEDIA_TYPE, MediaType.MOVIE);
            intent.putExtra(KEY_MEDIA_ID, movie.getId());
            intent.putExtra(KEY_MEDIA_TITLE, movie.getTitle());
            getContext().startActivity(intent);
        }
    }

}
