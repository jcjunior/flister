package br.com.flister.network;

import br.com.flister.model.Movies;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by junior on 21/12/2016.
 */

public interface TheMovieDatabaseAPI {

    @GET("/3/movie/upcoming")
    Call<Movies> getUpcomingMovies(@Query("api_key") String api_key );

}
