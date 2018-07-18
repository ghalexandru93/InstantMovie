package com.ghalexandru.instant_movie.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghalexandru.instant_movie.R;
import com.ghalexandru.instant_movie.adapter.Adapter;
import com.ghalexandru.instant_movie.model.media.Media;
import com.ghalexandru.instant_movie.model.media.MediaResponse;
import com.ghalexandru.instant_movie.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Base fragment class for all movie and tv show fragments
 */
public abstract class MediaFragment extends Fragment {
    private static final String TAG = MediaFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    protected int currentPage;
    protected int totalPages;
    private boolean loading;

    protected LinearLayoutManager layoutManager;
    protected Adapter mediaAdapter;

    protected ItemDecoration itemDecoration;

    @Override
    public void onResume() {
        super.onResume();

        refreshFragment();
    }

    @Override
    public void onPause() {
        super.onPause();

        clearProgressLoader();
    }

    /**
     * If {@link #mediaAdapter} has no items then we try to get some items
     *
     * @return get true if fragment will be refreshed, otherwise get false
     */
    private boolean refreshFragment() {
        if (mediaAdapter != null && mediaAdapter.getItemCount() == 0) {
            currentPage = 0;
            totalPages = 0;

            getMedia();
            return true;
        }

        return false;
    }

    /**
     * Set the field {@link #loading} to be true and show a visual refreshing
     * by setting {@link #refreshLayout} to refresh
     */
    protected void showProgressLoader() {
        loading = true;
        refreshLayout.setRefreshing(true);
    }

    /**
     * Set the field {@link #loading} to be false and stop the visual refreshing
     * of the field {@link #refreshLayout}
     */
    protected void hideProgressLoader() {
        loading = false;
        refreshLayout.setRefreshing(false);
    }

    /**
     * Stop {@link #refreshLayout} from showing visual representation of refreshing
     */
    private void clearProgressLoader() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout.destroyDrawingCache();
            refreshLayout.clearAnimation();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * every derived fragment will inflate {@code R.layout.fragment_media}
         */
        View view = inflater.inflate(R.layout.fragment_media, container, false);

        /**
         * make this fragment to not be destroyed from changing phone orientation
         */
        setRetainInstance(true);

        ButterKnife.bind(this, view);

        /**
         * set color and listener for {@link #refreshLayout}
         */
        refreshLayout.setColorSchemeResources(R.color.primary);
        refreshLayout.setOnRefreshListener(refreshListener);

        setupSpaceDecoration();
        setupRecyclerView();
        updateItemDecoration();

        return view;
    }

    /**
     * Listener for {@link #refreshLayout}
     */
    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
            if (!refreshFragment())
                hideProgressLoader();
        }

    };

    public void update() {
        updateLayoutManager();
        updateItemDecoration();
    }

    /**
     * Make a server request to retrieve a list of movies or tv shows
     * that will be received in {@link #mediaCallback}
     */
    protected abstract void getMedia();

    /**
     * Each derived fragment will provide an implementation of this method
     * that will setup {@link #recyclerView} for some specific needs
     */
    protected abstract void setupRecyclerView();

    /**
     * Change {@link #layoutManager} configuration
     */
    private void updateLayoutManager() {
        Parcelable state = layoutManager.onSaveInstanceState();
        layoutManager = new GridLayoutManager(getContext(), getNumberOfColumns(getContext()));
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.onRestoreInstanceState(state);
    }

    protected void updateItemDecoration() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(Util.USER_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        int recycledViewMode = sharedPref.getInt(Util.RECYCLE_VIEW_MODE, Util.RECYCLE_VIEW_LIST);

        if (recycledViewMode == Util.RECYCLE_VIEW_GRID) {

            /**
             * add {@link #itemDecoration} to the {@link #recyclerView}
             */
            recyclerView.addItemDecoration(itemDecoration);

            /**
             * padding for {@link #recyclerView}
             */
            float recycleViewPadding = getResources().getDimension(R.dimen.recycle_view_padding);

            /**
             * get {@link R.dimen.grid_recycler_padding_ration} and convert as a float;
             */
            TypedValue outValue = new TypedValue();
            getResources().getValue(R.dimen.grid_recycler_padding_ration, outValue, true);

            /**
             * this value is a ratio that will be multiplied with {@link recycleViewPadding}
             */
            float ratio = outValue.getFloat();

            recycleViewPadding *= ratio;

            /**
             * set padding for {@link #recyclerView}
             */
            recyclerView.setPadding((int) recycleViewPadding, (int) (recycleViewPadding / 1.2f),
                    (int) recycleViewPadding, (int) (recycleViewPadding / 1.2f));

        } else if (recycledViewMode == Util.RECYCLE_VIEW_LIST) {

            /**
             * remove any decoration and padding for {@link #recyclerView}
             */
            recyclerView.removeItemDecoration(itemDecoration);
            recyclerView.setPadding(0, 0, 0, 0);
        }
    }

    protected void setupSpaceDecoration() {
        float spaceDecorationValue = getResources().getDimension(R.dimen.grid_space_decoration);

        /**
         * get {@link R.dimen.grid_space_decoration_ratio} and convert as a float;
         */
        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.grid_space_decoration_ratio, outValue, true);

        /**
         * this value is a ratio that will be multiplied with {@link spaceDecorationValue}
         */
        float ratio = outValue.getFloat();

        spaceDecorationValue *= ratio;

        itemDecoration = new ItemDecoration((int) spaceDecorationValue);
    }

    /**
     * Return nr. of columns based on screen density
     */
    protected int getNumberOfColumns(Context context) {

        //get screen width
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float widthPx = displayMetrics.widthPixels;

        // calculate desired width
        SharedPreferences sharedPref = context.getSharedPreferences(Util.USER_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        if (sharedPref.getInt(Util.RECYCLE_VIEW_MODE, Util.RECYCLE_VIEW_LIST) == Util.RECYCLE_VIEW_LIST) {

            float desiredPx = context.getResources().getDimensionPixelSize(R.dimen.movie_list_size);
            int columns = Math.round(widthPx / desiredPx);
            return columns > 1 ? columns : 1;

        } else {
            float desiredPx = context.getResources().getDimensionPixelSize(R.dimen.movie_grid_size);

            int columns = Math.round(widthPx / desiredPx);
            return columns > 3 ? columns : 3;
        }
    }

    protected Callback<MediaResponse> mediaCallback = new Callback<MediaResponse>() {
        @Override
        public void onResponse(@NonNull Call<MediaResponse> call, Response<MediaResponse> response) {
            MediaResponse mediaResponse = response.body();

            if (mediaResponse != null) {

                totalPages = mediaResponse.getTotalPages();
                currentPage = mediaResponse.getPage();
                List<Media> results = response.body().getResults();
                mediaAdapter.add(new ArrayList<>(results));

                Log.i(TAG, "Current page " + currentPage);
            }

            hideProgressLoader();
        }

        @Override
        public void onFailure(@NonNull Call<MediaResponse> call, @NonNull Throwable t) {
            hideProgressLoader();
        }
    };

    /**
     * Callback for {@link #recyclerView} that will make retrofit to call next currentPage
     * if {@link #recyclerView} reach bottom of visible items
     */
    protected RecyclerView.OnScrollListener recyclerCallback = new RecyclerView.OnScrollListener() {
        private int firstVisibleItem;
        private int totalItemCount;
        private int visibleItemCount;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (loading) return;

            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = layoutManager.getItemCount();
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

            if (currentPage < totalPages && firstVisibleItem + visibleItemCount >= totalItemCount)
                getMedia();
        }
    };

    protected class ItemDecoration extends RecyclerView.ItemDecoration {
        int horizontalSpacing;
        int verticalSpacing;

        ItemDecoration(int spacing) {
            this(spacing, spacing);
        }

        ItemDecoration(int horizontalSpacing, int verticalSpacing) {
            this.horizontalSpacing = horizontalSpacing;
            this.verticalSpacing = verticalSpacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);


            if (parent.getLayoutManager() instanceof GridLayoutManager) {

                GridLayoutManager gridLayoutManager = ((GridLayoutManager) parent.getLayoutManager());
                int spanCount = gridLayoutManager.getSpanCount();

                /**
                 * invalid span count
                 */
                if (spanCount < 1) return;

                int itemPos = parent.getChildAdapterPosition(view);
                GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

                /**
                 * add top spacing for items except for first row
                 */
                if (spanSizeLookup.getSpanGroupIndex(itemPos, spanCount) == 0)
                    outRect.top = 0;
                else
                    outRect.top = verticalSpacing;

                outRect.bottom = 0;

                /**
                 * to which span the left border of this item belongs
                 */
                int spanIndexLeft = spanSizeLookup.getSpanIndex(itemPos, spanCount);
                outRect.left = horizontalSpacing * spanIndexLeft / spanCount;

                /**
                 * to which span the right border of this item belongs
                 */
                int spanIndexRight = spanIndexLeft - 1 + spanSizeLookup.getSpanSize(itemPos);
                outRect.right = horizontalSpacing * (spanCount - spanIndexRight - 1) / spanCount;

            } else {
                outRect.left = horizontalSpacing;
                outRect.right = horizontalSpacing;
                outRect.bottom = horizontalSpacing;

                /**
                 * add top margin only for the first item to avoid double space between items
                 */
                if (parent.getChildAdapterPosition(view) == 0)
                    outRect.top = horizontalSpacing;

            }

        }
    }
}
