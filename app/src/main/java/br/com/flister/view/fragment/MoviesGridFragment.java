package br.com.flister.view.fragment;

import android.app.Application;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.flister.R;
import br.com.flister.broadcast.GetUpcomingMoviesReceiver;
import br.com.flister.delegate.GetUpcomingMoviesDelegate;
import br.com.flister.model.MovieGridItemVO;
import br.com.flister.network.MovieListRest;
import br.com.flister.view.adapter.MovieAdapter;

/**
 * Created by junior on 21/12/2016.
 */

@EFragment(R.layout.fragment_movie_grid)
public class MoviesGridFragment extends Fragment implements GetUpcomingMoviesDelegate {

    private static final String MOVIE_LIST = "movie_list";

    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bean
    MovieListRest movieListRest;

    @Bean
    MovieAdapter movieAdapter;

    @ViewById(R.id.poweredByTMD)
    ImageView poweredByTMD;

    private GetUpcomingMoviesReceiver getUpcomingMoviesReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUpcomingMoviesReceiver = GetUpcomingMoviesReceiver.registerObserver(this);

        movieListRest.getUpcomingMovies();

    }

    @AfterViews
    public void onCreateView(){
        Glide.with(this).load(R.drawable.powered).into(poweredByTMD);
    }

    @Override
    public void manageGetUpComingMoviesSuccess(List<MovieGridItemVO> moviesVO) {

        if (moviesVO != null) {

            movieAdapter.setMovies(moviesVO);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(movieAdapter);
        }

    }

    @Override
    public void managerGetUpComingMoviesCallbackError(Exception e) {
        Log.i("WEBSERVICE error", e.toString());
    }

    @Override
    public Application retrieveApplication() {
        return getActivity().getApplication();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getUpcomingMoviesReceiver.unregisterObserver(retrieveApplication());
    }





    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
