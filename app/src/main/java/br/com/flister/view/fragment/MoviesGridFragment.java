package br.com.flister.view.fragment;

import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
import br.com.flister.view.adapter.MovieAdapter;

/**
 * Created by junior on 21/12/2016.
 */

@EFragment(R.layout.fragment_movie_grid)
public class MoviesGridFragment extends Fragment implements GenericMoviesDelegate {

    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUpcomingMoviesReceiver = new GetUpcomingMoviesReceiver();
        getUpcomingMoviesReceiver.registerObserver(this);

        getFavoriteMoviesReceiver = new GetFavoriteMoviesReceiver();
        getFavoriteMoviesReceiver.registerObserver(this);

        getRecentMoviesReceiver = new GetRecentMoviesReceiver();
        getRecentMoviesReceiver.registerObserver(this);
    }

    @AfterViews
    public void onCreateView(){
        Glide.with(this).load(R.drawable.powered).into(poweredByTMD);
        movieAdapter.setFragment(this);

        switch (dataOrigin){

            case DATA_BASE:
                movieServiceImpl.getFavoriteMovies();
                break;

            case REST_API:
                movieServiceImpl.getUpcomingMovies();
                break;

            case SHARED_PREFERENCES:
                movieServiceImpl.getLastVisitedMovie();
                break;

            default:
                Toast.makeText(getActivity(),"Something went wrong", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void initializeMovieGrid(List<MovieGridItemVO> moviesVO) {

        if (recyclerView != null) {
            movieAdapter.setMovies(moviesVO);
            movieAdapter.notifyDataSetChanged();

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(movieAdapter);
        }
    }

    @Override
    public void manageGetMoviesSuccess(List<MovieGridItemVO> movies) {
        if (movies != null && movies.size() > 0){
            initializeMovieGrid(movies);
        }
    }

    @Override
    public void managerGetMoviesError(Exception e) {
        Toast.makeText(getActivity(), "An error occured "+ e, Toast.LENGTH_SHORT).show();
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
