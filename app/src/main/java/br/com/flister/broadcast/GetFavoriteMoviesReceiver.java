package br.com.flister.broadcast;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.List;

import br.com.flister.delegate.GenericMoviesDelegate;
import br.com.flister.model.MovieGridItemVO;

/**
 * Created by junior on 22/12/2016.
 */

public class GetFavoriteMoviesReceiver extends GenericMovieReceiver {

    private static final String TAG = GetFavoriteMoviesReceiver.class.getSimpleName();

    public static final String GET_FAVORITE_MOVIES_RECEIVER = "GET FAVORITE MOVIES RECEIVER";
    public static final String GET_FAVORITE_MOVIES_RESPONSE = "GET FAVORITE MOVIES RESPONSE";
    public static final String GET_FAVORITE_MOVIES_EXCEPTION = "GET FAVORITE MOVIES EXCEPTION";

    @Override
    public String initObserverName() {
        return GET_FAVORITE_MOVIES_RECEIVER;
    }

    @Override
    public String initResponseName() {
        return GET_FAVORITE_MOVIES_RESPONSE;
    }

    @Override
    public String initExceptionName() {
        return GET_FAVORITE_MOVIES_EXCEPTION;
    }

    @Override
    public GetFavoriteMoviesReceiver registerObserver(GenericMoviesDelegate delegate) {

        GetFavoriteMoviesReceiver receiver = null;

        try {

            receiver = new GetFavoriteMoviesReceiver();
            receiver.delegate = delegate;
            LocalBroadcastManager.getInstance(delegate.retrieveApplication())
                    .registerReceiver(receiver, new IntentFilter(GET_FAVORITE_MOVIES_RECEIVER));

        } catch(Throwable ex) {
            Log.e(TAG, "Error on register observer [" + GET_FAVORITE_MOVIES_RECEIVER + "]", ex);
        }

        return receiver;
    }
}
