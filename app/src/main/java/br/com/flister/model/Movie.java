package br.com.flister.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by junior on 21/12/2016.
 */

public class Movie {

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("overview")
    public String overview;

    @SerializedName("title")
    public String title;

    @SerializedName("release_date")
    public String releaseDate;

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
