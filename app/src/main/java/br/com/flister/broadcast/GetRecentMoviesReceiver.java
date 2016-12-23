package br.com.flister.broadcast;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import br.com.flister.delegate.GenericMoviesDelegate;

/**
 * Created by junior on 21/12/2016.
 */

public class GetRecentMoviesReceiver extends GenericMovieReceiver {

    private static final String TAG = GetRecentMoviesReceiver.class.getSimpleName();

    public static final String GET_RECENT_MOVIE_OBSERVER = "GET RECENT RECEIVER OBSERVER";
    public static final String GET_RECENT_MOVIE_RESPONSE = "GET RECENT MOVIE RESPONSE";
    public static final String GET_RECENT_MOVIE_EXCEPTION = "GET RECENT MOVIE EXCEPTION";

    @Override
    public String initObserverName() {
        return GET_RECENT_MOVIE_OBSERVER;
    }

    @Override
    public String initResponseName() {
        return GET_RECENT_MOVIE_RESPONSE;
    }

    @Override
    public String initExceptionName() {
        return GET_RECENT_MOVIE_EXCEPTION;
    }

    @Override
    public GenericMovieReceiver registerObserver(GenericMoviesDelegate delegate) {

        GetRecentMoviesReceiver receiver = null;

        try {

            receiver = new GetRecentMoviesReceiver();
            receiver.delegate = delegate;
            LocalBroadcastManager.getInstance(delegate.retrieveApplication())
                    .registerReceiver(receiver, new IntentFilter(GET_RECENT_MOVIE_OBSERVER));

        } catch(Throwable ex) {
            Log.e(TAG, "Error on register observer [" + GET_RECENT_MOVIE_OBSERVER + "]", ex);
        }

        return receiver;
    }
}
