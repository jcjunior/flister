package br.com.flister.delegate;

import android.app.Application;

import java.util.List;

import br.com.flister.model.MovieGridItemVO;

/**
 * Created by junior on 21/12/2016.
 */

public interface GetUpcomingMoviesDelegate {

    void manageGetUpComingMoviesSuccess(List<MovieGridItemVO> moviesVO);
    void managerGetUpComingMoviesCallbackError(Exception e);
    Application retrieveApplication();

}
