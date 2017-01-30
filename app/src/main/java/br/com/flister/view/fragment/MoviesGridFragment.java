package br.com.flister.view.fragment;

import android.app.Application;
import android.app.Fragment;
import android.content.res.Configuration;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.flister.R;
import br.com.flister.broadcast.GetFavoriteMoviesReceiver;
import br.com.flister.broadcast.GetRecentMoviesReceiver;
import br.com.flister.broadcast.GetUpcomingMoviesReceiver;
import br.com.flister.delegate.GenericMoviesDelegate;
import br.com.flister.model.MovieGridItemVO;
import br.com.flister.service.MovieServiceImpl;
import br.com.flister.utils.Constants;
import br.com.flister.utils.DataOrigin;
import br.com.flister.view.activity.MainActivity;
import br.com.flister.view.adapter.MovieAdapter;

/**
 * Created by junior on 21/12/2016.
 */

@EFragment(R.layout.fragment_movie_grid)
public class MoviesGridFragment extends Fragment implements GenericMoviesDelegate {

    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;

    @ViewById(R.id.progressBarGrid)
    ProgressBar progressBar;

    @ViewById(R.id.lblEmptyContent)
    TextView lblEmptyContent;

    @Bean
    MovieAdapter movieAdapter;

    @Bean
    MovieServiceImpl movieServiceImpl;

    @ViewById(R.id.poweredByTMD)
    ImageView poweredByTMD;

    @FragmentArg(Constants.DATA_ORIGIN)
    DataOrigin dataOrigin;

    private GetUpcomingMoviesReceiver getUpcomingMoviesReceiver;
    private GetFavoriteMoviesReceiver getFavoriteMoviesReceiver;
    private GetRecentMoviesReceiver getRecentMoviesReceiver;

    @AfterViews
    public void onCreateView(){

        getFavoriteMoviesReceiver = new GetFavoriteMoviesReceiver();
        getUpcomingMoviesReceiver = new GetUpcomingMoviesReceiver();
        getRecentMoviesReceiver = new GetRecentMoviesReceiver();

        //Glide.with(this).load(R.drawable.powered).into(poweredByTMD);
        Picasso.with(getActivity())
                .load(R.drawable.powered)
                .into(poweredByTMD);

        switch (dataOrigin) {

            case DATA_BASE:
                getFavoriteMoviesReceiver.registerObserver(this);
                movieServiceImpl.getFavoriteMovies();
                break;

            case REST_API:
                getUpcomingMoviesReceiver.unregisterObserver(retrieveApplication());
                getUpcomingMoviesReceiver.registerObserver(this);
                movieServiceImpl.getUpcomingMovies();
                break;

            case SHARED_PREFERENCES:
                getRecentMoviesReceiver.registerObserver(this);
                movieServiceImpl.getLastVisitedMovie();
                break;

            default:
                ((MainActivity) getActivity()).getSnackBar("Something went wrong").setAction("Close", clickListener).show();
                break;
        }

    }

    final View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
        }
    };

    private void initializeMovieGrid(List<MovieGridItemVO> movies) {

        if (recyclerView != null) {

            int number_of_columns;

            recyclerView.setVisibility(View.VISIBLE);
            movieAdapter.setMainActivity((MainActivity) getActivity());
            movieAdapter.setMovies(movies);
            movieAdapter.notifyDataSetChanged();

            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            int orientation = getResources().getConfiguration().orientation;

            if (tabletSize) {

                if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                    number_of_columns = 4;
                } else {
                    number_of_columns = 3;
                }
            } else {

                if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                    number_of_columns = 3;
                } else {
                    number_of_columns = 2;
                }
            }

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), number_of_columns);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(movieAdapter);
        }
    }

    @Override
    public void manageGetMoviesSuccess(List<MovieGridItemVO> movies) {

        if (progressBar != null) progressBar.setVisibility(View.GONE);

        if (movies != null && movies.size() > 0){
            if (lblEmptyContent != null) lblEmptyContent.setVisibility(View.GONE);
            initializeMovieGrid(movies);
        } else {
            if (lblEmptyContent != null) lblEmptyContent.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void managerGetMoviesError(Exception e) {
        ((MainActivity) getActivity()).getSnackBar("An error occured "+ e).setAction("Close", clickListener).show();
    }

    @Override
    public Application retrieveApplication() {
        return getActivity().getApplication();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getUpcomingMoviesReceiver.unregisterObserver(retrieveApplication());
        getFavoriteMoviesReceiver.unregisterObserver(retrieveApplication());
        getRecentMoviesReceiver.unregisterObserver(retrieveApplication());
    }

}
