package br.com.flister.model;

import java.io.Serializable;

/**
 * Created by junior on 21/12/2016.
 */

public class MovieGridItemVO implements Serializable {

    public String title;
    public String subtitle;
    public String overview;
    public String poster;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
