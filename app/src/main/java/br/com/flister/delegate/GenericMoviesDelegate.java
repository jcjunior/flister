package br.com.flister.delegate;

import android.app.Application;

import java.util.List;

import br.com.flister.model.MovieGridItemVO;

/**
 * Created by junior on 22/12/2016.
 */

public interface GenericMoviesDelegate {

    void manageGetMoviesSuccess(List<MovieGridItemVO> movies);
    void managerGetMoviesError(Exception e);
    Application retrieveApplication();
}
