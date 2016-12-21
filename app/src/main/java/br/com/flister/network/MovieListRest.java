package br.com.flister.network;

import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import br.com.flister.BuildConfig;
import br.com.flister.broadcast.GetUpcomingMoviesReceiver;
import br.com.flister.model.Movie;
import br.com.flister.model.MovieGridItemVO;
import br.com.flister.model.Movies;
import br.com.flister.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by junior on 21/12/2016.
 */
@EBean
public class MovieListRest {

    private static final String TAG = MovieListRest.class.getSimpleName();
    private static final String APP_KEY = BuildConfig.MOVIE_DB_API_KEY;

    @RootContext
    Context context;

    TheMovieDatabaseAPI theMovieDatabaseAPI;

    public MovieListRest(){
        theMovieDatabaseAPI = RestAPIClient.getClient().create(TheMovieDatabaseAPI.class);
    }

    public void getUpcomingMovies(){

        Call<Movies> call = theMovieDatabaseAPI.getUpcomingMovies(APP_KEY);

        call.enqueue(new Callback<Movies>() {

            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {

                if (response != null
                        && response.body() != null
                        && response.body().getMovies() != null){

                    List<MovieGridItemVO> movies = parseMoviesIntoVO(response.body().getMovies());

                    GetUpcomingMoviesReceiver.notifyNewParametersReceived(context, movies);

                    Log.i(TAG, call.request().toString());
                    Log.i(TAG, "On response getUpcomingMovies ["+response.body().toString()+"]");
                }

            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

                GetUpcomingMoviesReceiver.notifyErrorReceived(context, (Exception) t);

                Log.i(TAG, call.request().toString());
                Log.i(TAG, "On Failure getUpcomingMovies", t);
            }
        });
    }

    private List<MovieGridItemVO> parseMoviesIntoVO(List<Movie> movies) {

        List<MovieGridItemVO> moviesVO = new ArrayList<>();

        for (Movie movie: movies) {

            MovieGridItemVO movieVO = new MovieGridItemVO();
            movieVO.setOverview(movie.getOverview());
            movieVO.setTitle(movie.getTitle());
            movieVO.setPoster(Constants.BASE_GET_IMAGE_URL+movie.getPosterPath());
            movieVO.setSubtitle(movie.getTitle());

            moviesVO.add(movieVO);
        }


        return moviesVO;
    }
}
