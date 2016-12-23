package br.com.flister.broadcast;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import br.com.flister.delegate.GenericMoviesDelegate;
import br.com.flister.model.MovieGridItemVO;

/**
 * Created by junior on 22/12/2016.
 */

public abstract class GenericMovieReceiver extends BroadcastReceiver {

    private static final String TAG = GenericMovieReceiver.class.getSimpleName();

    protected GenericMoviesDelegate delegate;

    private final String OBSERVER_NAME = initObserverName();
    private final String RESPONSE_NAME = initResponseName();
    private final String EXCEPTION_NAME = initExceptionName();

    public abstract String initObserverName();
    public abstract String initResponseName();
    public abstract String initExceptionName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Exception exception = (Exception) intent.getSerializableExtra(EXCEPTION_NAME);

        if (exception == null){
            delegate.manageGetMoviesSuccess((List<MovieGridItemVO>) intent.getSerializableExtra(RESPONSE_NAME));
        } else {
            delegate.managerGetMoviesError(exception);
        }
    }

    /**
     * Registers a new broadcast receiver.
     *
     * @param delegate
     */
    public abstract GenericMovieReceiver registerObserver(GenericMoviesDelegate delegate);

    /**
     * Unregister a broadcast receiver that was previously registered.
     *
     * @param application
     */
    public void unregisterObserver(Application application){

        try {

            LocalBroadcastManager.getInstance(application).unregisterReceiver(this);

        } catch(Throwable ex) {
            Log.e(TAG, "Error on unregister observer [" + OBSERVER_NAME + "]", ex);
        }

    }

    /**
     * Notify a registered observer that exist new data
     *
     * @param context
     * @param result
     */
    public void notifyNewParametersReceived(Context context, List<MovieGridItemVO> result){

        try {

            Intent intent = new Intent(OBSERVER_NAME);
            intent.putExtra(RESPONSE_NAME, (Serializable) result);

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        } catch(Throwable ex) {
            Log.e(TAG, "Error on notify new data to observer [" + OBSERVER_NAME + "]", ex);
        }
    }

    /**
     * Notify a registered observer that occurred an error
     *
     * @param context
     * @param e
     */
    public void notifyErrorReceived(Context context, Exception e){

        try {

            Intent intent = new Intent(OBSERVER_NAME);
            intent.putExtra(EXCEPTION_NAME, e);

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch(Throwable ex) {
            Log.e(TAG, "Error on notify an error to observer [" + OBSERVER_NAME + "]", ex);
        }
    }
}