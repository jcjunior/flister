package br.com.flister.view.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import br.com.flister.R;
import br.com.flister.model.MovieGridItemVO;
import br.com.flister.service.MovieServiceImpl;
import br.com.flister.utils.Constants;
import br.com.flister.utils.DataOrigin;
import br.com.flister.utils.MoviesPreferences_;
import br.com.flister.view.activity.MainActivity;
import br.com.flister.view.fragment.MovieOverviewFragment;
import br.com.flister.view.fragment.MovieOverviewFragment_;
import br.com.flister.view.fragment.MoviesGridFragment;
import br.com.flister.view.fragment.MoviesGridFragment_;

/**
 * Created by junior on 21/12/2016.
 */

@EBean
public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<MovieGridItemVO> movies;

    @RootContext
    Context context;

    @Pref
    MoviesPreferences_ moviesPreference;

    private MainActivity mainActivity;

    @Bean
    MovieServiceImpl movieServiceImpl;


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {

        final MovieGridItemVO movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());

        Picasso.with(context)
                .load(Constants.BASE_GET_IMAGE_URL + movie.getPoster())
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);

        //Glide.with(context).load(Constants.BASE_GET_IMAGE_URL + movie.getPoster()).placeholder(R.drawable.placeholder).crossFade().into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, MovieGridItemVO movie) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_movie, popup.getMenu());

        MenuItem remove_favorite = popup.getMenu().findItem(R.id.action_remove_favorite);
        MenuItem add_favorite = popup.getMenu().findItem(R.id.action_add_favorite);

        if (movie.isFavorite()){
            remove_favorite.setVisible(true);
            add_favorite.setVisible(false);
        } else {
            remove_favorite.setVisible(false);
            add_favorite.setVisible(true);
        }

        popup.setOnMenuItemClickListener(new MovieAdapter.MyMenuItemClickListener(movie));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private MovieGridItemVO movieGridItemVO;

        public MyMenuItemClickListener(MovieGridItemVO movie) {
            this.movieGridItemVO = movie;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            try {
                switch (menuItem.getItemId()) {

                    case R.id.action_add_favorite:
                        movieGridItemVO.setFavorite(true);
                        movieServiceImpl.insertOrUpdate(movieGridItemVO);
                        mainActivity.getSnackBar("Movie saved on favorites with success").setAction("Close", clickListener).show();;
                        return true;

                    case R.id.action_overview:
                        Set<String> movieIDs = moviesPreference.movies_ids().get();
                        movieIDs.add(movieGridItemVO.getIdMovie());
                        moviesPreference.edit().movies_ids().put(movieIDs).apply();
                        callMovieOverviewFragment(movieGridItemVO);
                        return true;

                    case R.id.action_remove_favorite:
                        int rows = movieServiceImpl.remove(movieGridItemVO);
                        if (rows == 1){
                            mainActivity.getSnackBar("Movie removed from favorites with success").setAction("Close", clickListener).show();;
                        }
                        callFavoritesFragment();
                        return true;

                    default:
                }

            } catch (SQLException e) {
                mainActivity.getSnackBar("An error occurred in database. ["+e.getMessage()+"]").setAction("Close", clickListener).show();;
                Log.e(TAG, e.getMessage());
            }

            return false;
        }

        final View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
            }
        };
    }

    public void setMovies(List<MovieGridItemVO> movies) {
        this.movies = movies;
    }

    private void callMovieOverviewFragment(MovieGridItemVO movie) {

        MovieOverviewFragment movieOverviewFragment = MovieOverviewFragment_.builder()
                .movieSelected(movie)
                .build();

        mainActivity.getFragmentManager()
                .beginTransaction()
                .replace(R.id.myScrollingContent, movieOverviewFragment)
                .commit();

        Log.d(TAG, "MovieAdapter called MovieOverviewFragment with success");
    }

    private void callFavoritesFragment() {

        MoviesGridFragment moviesGridFragment = MoviesGridFragment_.builder()
                .dataOrigin(DataOrigin.DATA_BASE)
                .build();

        mainActivity.getFragmentManager()
                .beginTransaction()
                .replace(R.id.myScrollingContent, moviesGridFragment)
                .commit();

        Log.d(TAG, "MovieAdapter called MovieOverviewFragment with success");
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
