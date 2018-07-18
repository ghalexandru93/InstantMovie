package com.ghalexandru.instant_movie.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ghalexandru.instant_movie.GlideApp;
import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.activity.DetailActivity;
import com.ghalexandru.instant_movie.model.Model;
import com.ghalexandru.instant_movie.model.media.Media;
import com.ghalexandru.instant_movie.util.MediaType;
import com.ghalexandru.instant_movie.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_ID;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TITLE;
import static com.ghalexandru.instant_movie.util.Util.KEY_MEDIA_TYPE;
import static com.ghalexandru.instant_movie.util.Util.RECYCLE_VIEW_GRID;
import static com.ghalexandru.instant_movie.util.Util.RECYCLE_VIEW_LIST;
import static com.ghalexandru.instant_movie.util.Util.RECYCLE_VIEW_MODE;
import static com.ghalexandru.instant_movie.util.Util.USER_SHARED_PREFERENCE;
import static com.ghalexandru.instant_movie.util.Util.getAString;
import static com.ghalexandru.instant_movie.util.Util.getMovieGenres;


/**
 * RecyclerView adapter for movies or tv shows
 */
public class MediaAdapter extends Adapter<MediaAdapter.ViewHolder> {

    private final SharedPreferences sharedPref;
    private MediaType mediaType;

    /**
     * if this is false then only {@code RECYCLE_VIEW_GRID} will be used
     */
    private boolean useItemViewType;

    private List<Media> list;

    private int lastPosition = -1;

    public MediaAdapter(Context context, MediaType mediaType, boolean useItemViewType) {
        sharedPref = context.getSharedPreferences(USER_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        this.mediaType = mediaType;
        this.useItemViewType = useItemViewType;
        list = new ArrayList<>();
    }

    /**
     * Add objects that extends {@link Media}
     */
    @Override
    public void add(List<? extends Model> list) {
        int oldPosition = this.list.size() + 1;
        this.list.addAll((Collection<? extends Media>) list);
        notifyItemRangeInserted(oldPosition, list.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewType)
            return RECYCLE_VIEW_GRID;
        else
            return (sharedPref.getInt(RECYCLE_VIEW_MODE, RECYCLE_VIEW_LIST));
    }

    /**
     * Inflate {@code R.layout.item_media_list} or {@code R.layout.item_media_grid}
     * based on {@param viewType}
     */
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == RECYCLE_VIEW_LIST)
            return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_list, parent, false));
        else if (viewType == RECYCLE_VIEW_GRID)
            return new GridViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_grid, parent, false));

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));

        if (getItemViewType(position) == RECYCLE_VIEW_LIST)
            setAnimation(holder.itemView, position);
    }

    /**
     * Animation for current view
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position >= lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                    R.anim.scale_anim);

            viewToAnimate.startAnimation(animation);

            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Remove all media from {@link #list}
     */
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }

    /**
     * ViewHolder for list movies or tv shows
     */
    class ListViewHolder extends ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.iv_poster)
        ImageView ivPoster;

        @BindView(R.id.tv_rating)
        TextView tvRating;

        @BindView(R.id.tv_date)
        TextView tvDate;

        @BindView(R.id.tv_genres)
        TextView tvGenres;

        ListViewHolder(View itemView) {
            super(itemView, mediaType);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(@NonNull final Media media) {
            super.bind(media);

            GlideApp
                    .with(ivPoster.getContext())
                    .load(Util.IMAGE_URL_PATH + media.getPosterPath())
                    .placeholder(null)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPoster);

            tvName.setText(getAString(media.getTitle()));
            tvDate.setText(Util.convertDate(media.getReleaseDate()));
            tvGenres.setText(getMovieGenres(media.getGenreIds()));
            tvRating.setText(getAString(media.getVoteAverage()));
        }
    }

    /**
     * ViewHolder for grid movies or tv shows
     */
    class GridViewHolder extends ViewHolder {

        @BindView(R.id.iv_poster)
        ImageView ivMoviePoster;

        GridViewHolder(View itemView) {
            super(itemView, mediaType);

            if (!useItemViewType) {
                int width, height;
                width = (int) itemView.getContext().getResources().getDimension(R.dimen.grid_poster_width);
                height = (int) itemView.getContext().getResources().getDimension(R.dimen.grid_poster_height);
                itemView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
            }

            ButterKnife.bind(this, itemView);

        }

        @Override
        public void bind(@NonNull final Media media) {
            super.bind(media);

            GlideApp
                    .with(ivMoviePoster.getContext())
                    .load(Util.IMAGE_URL_PATH + media.getPosterPath())
                    .placeholder(null)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivMoviePoster);
        }

    }

    /**
     * Base view holder class for {@link ListViewHolder} and {@link GridViewHolder}
     */
    static abstract class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * A movie or a tv show
         */
        private MediaType mediaType;

        ViewHolder(View itemView, MediaType mediaType) {
            super(itemView);
            this.mediaType = mediaType;
        }

        /**
         * Base bind method that will be overridden by extended classes
         *
         * @param media
         */
        public void bind(final Media media) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNewActivity(itemView.getContext(), media, mediaType);
                }
            });
        }

        /**
         * Start activity {@link DetailActivity}
         */
        private void startNewActivity(Context context, Media media, MediaType mediaType) {

            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(KEY_MEDIA_ID, media.getId());
            intent.putExtra(KEY_MEDIA_TYPE, mediaType);
            intent.putExtra(KEY_MEDIA_TITLE, media.getTitle());
            context.startActivity(intent);
        }

        /**
         * Clear animations for this view
         */
        void clearAnimation() {
            itemView.clearAnimation();
        }

    }

}

