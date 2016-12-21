package br.com.flister.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by junior on 21/12/2016.
 */

public class Movies {

    @SerializedName("results")
    public List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
