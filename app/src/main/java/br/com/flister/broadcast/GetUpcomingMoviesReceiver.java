package br.com.flister.broadcast;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import br.com.flister.delegate.GenericMoviesDelegate;

/**
 * Created by junior on 21/12/2016.
 */

public class GetUpcomingMoviesReceiver extends GenericMovieReceiver{

    private static final String TAG = GetUpcomingMoviesReceiver.class.getSimpleName();

    public static final String GET_UPCOMING_MOVIES_OBSERVER = "GET UPCOMING MOVIES OBSERVER";
    public static final String GET_UPCOMING_MOVIES_RESPONSE = "GET UPCOMING MOVIES RESPONSE";
    public static final String GET_UPCOMING_MOVIES_EXCEPTION = "GET UPCOMING MOVIES EXCEPTION";

    @Override
    public String initObserverName() {
        return GET_UPCOMING_MOVIES_OBSERVER;
    }

    @Override
    public String initResponseName() {
        return GET_UPCOMING_MOVIES_RESPONSE;
    }

    @Override
    public String initExceptionName() {
        return GET_UPCOMING_MOVIES_EXCEPTION;
    }

    @Override
    public GenericMovieReceiver registerObserver(GenericMoviesDelegate delegate) {

        GetUpcomingMoviesReceiver receiver = null;

        try {

            receiver = new GetUpcomingMoviesReceiver();
            receiver.delegate = delegate;
            LocalBroadcastManager.getInstance(delegate.retrieveApplication())
                    .registerReceiver(receiver, new IntentFilter(GET_UPCOMING_MOVIES_OBSERVER));

        } catch(Throwable ex) {
            Log.e(TAG, "Error on register observer [" + GET_UPCOMING_MOVIES_OBSERVER + "]", ex);
        }

        return receiver;
    }
}
