package br.com.flister.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by junior on 21/12/2016.
 */
@DatabaseTable(tableName = Movie.TABLE_NAME_MOVIES)
public class Movie implements Serializable {

    public static final String TABLE_NAME_MOVIES = "movies";
    public static final String FIELD_ID = "id";
    public static final String FIELD_POSTER_PATH = "posterPath";
    public static final String FIELD_OVERVIEW = "overview";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_RELEASE_DATE = "releaseDate";
    public static final String FIELD_ID_MOVIE = "idMovie";
    public static final String FIELD_FAVORITE = "isFavorite";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int xid;

    @SerializedName("id")
    @DatabaseField(columnName = FIELD_ID_MOVIE, dataType = DataType.STRING)
    private String idMovie;

    @SerializedName("poster_path")
    @DatabaseField(columnName = FIELD_POSTER_PATH, dataType = DataType.STRING)
    private String posterPath;

    @SerializedName("overview")
    @DatabaseField(columnName = FIELD_OVERVIEW, dataType = DataType.STRING)
    private String overview;

    @SerializedName("title")
    @DatabaseField(columnName = FIELD_TITLE, dataType = DataType.STRING)
    private String title;

    @SerializedName("release_date")
    @DatabaseField(columnName = FIELD_RELEASE_DATE, dataType = DataType.STRING)
    private String releaseDate;

    @DatabaseField(columnName = FIELD_FAVORITE)
    private boolean favorite;

    public Movie() {
    }

    public int getXid() {
        return xid;
    }

    public void setXid(int id) {
        this.xid = id;
    }

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

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "xid=" + xid +
                ", idMovie='" + idMovie + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", overview='" + overview + '\'' +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}
