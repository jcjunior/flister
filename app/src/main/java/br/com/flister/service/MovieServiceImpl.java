package br.com.flister.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import br.com.flister.BuildConfig;
import br.com.flister.broadcast.GetFavoriteMoviesReceiver;
import br.com.flister.broadcast.GetRecentMoviesReceiver;
import br.com.flister.broadcast.GetUpcomingMoviesReceiver;
import br.com.flister.dao.DatabaseHelper;
import br.com.flister.model.Movie;
import br.com.flister.model.MovieGridItemVO;
import br.com.flister.model.Movies;
import br.com.flister.network.RestAPIClient;
import br.com.flister.network.TheMovieDatabaseAPI;
import br.com.flister.utils.MoviesPreferences_;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by junior on 22/12/2016.
 */

@EBean
public class MovieServiceImpl {

    private static final String TAG = MovieServiceImpl.class.getSimpleName();
    private static final String APP_KEY = BuildConfig.MOVIE_DB_API_KEY;

    @RootContext
    Context context;

    @Pref
    MoviesPreferences_ moviesPreference;

    private TheMovieDatabaseAPI theMovieDatabaseAPI;
    private DatabaseHelper databaseHelper = null;

    private GetRecentMoviesReceiver recentMoviesReceiver;
    private GetUpcomingMoviesReceiver upcomingMoviesReceiver;
    private GetFavoriteMoviesReceiver favoriteMoviesReceiver;

    public MovieServiceImpl() {
        theMovieDatabaseAPI = RestAPIClient.getClient().create(TheMovieDatabaseAPI.class);
        recentMoviesReceiver = new GetRecentMoviesReceiver();
        upcomingMoviesReceiver = new GetUpcomingMoviesReceiver();
        favoriteMoviesReceiver = new GetFavoriteMoviesReceiver();
    }

    public void getUpcomingMovies(){

        Call<Movies> call = theMovieDatabaseAPI.getUpcomingMovies(APP_KEY);

        call.enqueue(new Callback<Movies>() {

            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {

                if (response != null
                        && response.body() != null
                        && response.body().getMovies() != null){

                    List<MovieGridItemVO> movies = parseListMoviesToListMovieGridItemVO(response.body().getMovies());

                    upcomingMoviesReceiver.notifyNewParametersReceived(context, movies);

                    Log.i(TAG, call.request().toString());
                    Log.i(TAG, "On response upcomingMoviesReceiver ["+response.body().movies.size()+" movies]");
                }

            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

                upcomingMoviesReceiver.notifyErrorReceived(context, (Exception) t);

                Log.i(TAG, call.request().toString());
                Log.i(TAG, "On Failure upcomingMoviesReceiver", t);
            }
        });
    }

    public void getLastVisitedMovie(){

        Set<String> movies_ids = moviesPreference.movies_ids().get();
        final List<MovieGridItemVO> movieGridItemVOList = new ArrayList<>();

        if (movies_ids.size() > 1){

            Iterator itr = movies_ids.iterator();

            while(itr.hasNext()) {

                String idMovie = (String) itr.next();

                if(idMovie != "") {

                    Call<Movie> call = theMovieDatabaseAPI.getMovieById(idMovie, APP_KEY);

                    call.enqueue(new Callback<Movie>() {

                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {

                            if (response != null
                                    && response.body() != null){

                                MovieGridItemVO movie = parseMovieToMovieGridItemVO(response.body());
                                movieGridItemVOList.add(movie);
                                recentMoviesReceiver.notifyNewParametersReceived(context, movieGridItemVOList);

                                Log.i(TAG, call.request().toString());
                                Log.i(TAG, "On response recentMoviesReceiver ["+response.body().toString()+"]");
                            }

                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {

                            recentMoviesReceiver.notifyErrorReceived(context, (Exception) t);

                            Log.i(TAG, call.request().toString());
                            Log.i(TAG, "On Failure recentMoviesReceiver", t);
                        }
                    });
                }
            }

        } else {
            recentMoviesReceiver.notifyNewParametersReceived(context, movieGridItemVOList);
        }

    }

    public void getFavoriteMovies(){

        try {

            List<Movie> movies = getHelper().getMovieDAO().queryForAll();
            List<MovieGridItemVO> movieGridItemVOList = parseListMoviesToListMovieGridItemVO(movies);
            favoriteMoviesReceiver.notifyNewParametersReceived(context, movieGridItemVOList);
            Log.i(TAG, "On response getFavoriteMoviesReceiver ["+movieGridItemVOList.size()+" - movies]");

        } catch (SQLException e) {
            favoriteMoviesReceiver.notifyErrorReceived(context, e);
        }

    }

    public void insertOrUpdate(MovieGridItemVO movieGridItemVO) throws SQLException {

        Movie movie = parseMovieGridItemVOToMovie(movieGridItemVO);
        List<Movie> foundMovie = getHelper().getMovieDAO().queryForMatching(movie);

        if (foundMovie == null || foundMovie.size() == 0){
            getHelper().getMovieDAO().create(movie);
        } else {
            getHelper().getMovieDAO().update(movie);
        }

    }

    public int remove(MovieGridItemVO movieGridItemVO) throws SQLException {

        Movie movie = parseMovieGridItemVOToMovie(movieGridItemVO);
        List<Movie> foundMovie = getHelper().getMovieDAO().queryForMatching(movie);

        if (foundMovie != null && foundMovie.size() == 1){

            int rows = getHelper().getMovieDAO().delete(foundMovie.get(0));

            return rows;
        }

        return 0;

    }

    private List<MovieGridItemVO> parseListMoviesToListMovieGridItemVO(List<Movie> movies) {

        List<MovieGridItemVO> movieGridItemVOList = new ArrayList<>();

        for (Movie movie: movies) {

            movieGridItemVOList.add(parseMovieToMovieGridItemVO(movie));
        }

        return movieGridItemVOList;
    }

    private MovieGridItemVO parseMovieToMovieGridItemVO(Movie movie){

        MovieGridItemVO movieGridItemVO = new MovieGridItemVO();

        if (movie != null) {

            movieGridItemVO.setTitle(movie.getTitle());
            movieGridItemVO.setOverview(movie.getOverview());
            movieGridItemVO.setPoster(movie.getPosterPath());
            movieGridItemVO.setReleaseDate(movie.getReleaseDate());
            movieGridItemVO.setIdMovie(movie.getIdMovie());
            movieGridItemVO.setFavorite(movie.isFavorite());
        }

        return movieGridItemVO;
    }

    private Movie parseMovieGridItemVOToMovie(MovieGridItemVO movieGridItemVO){

        Movie movie = new Movie();

        if (movieGridItemVO != null) {

            movie.setTitle(movieGridItemVO.getTitle());
            movie.setOverview(movieGridItemVO.getOverview());
            movie.setPosterPath(movieGridItemVO.getPoster());
            movie.setReleaseDate(movieGridItemVO.getReleaseDate());
            movie.setIdMovie(movieGridItemVO.getIdMovie());
            movie.setFavorite(movieGridItemVO.isFavorite());
        }

        return movie;
    }

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private void releaseDatabase(){
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }


}
