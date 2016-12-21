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

import br.com.flister.delegate.GetUpcomingMoviesDelegate;
import br.com.flister.model.MovieGridItemVO;

/**
 * Created by junior on 21/12/2016.
 */

public class GetUpcomingMoviesReceiver extends BroadcastReceiver {

    private static final String TAG = GetUpcomingMoviesReceiver.class.getSimpleName();

    private GetUpcomingMoviesDelegate delegate;

    private static final String GET_UPCOMING_MOVIES_OBSERVER = "UPCOMING MOVIES OBSERVER";
    private static final String GET_UPCOMING_MOVIES_RESPONSE = "UPCOMING MOVIES RESPONSE";
    private static final String EXCEPTION = "EXCEPTION";

    @Override
    public void onReceive(Context context, Intent intent) {

        Exception exception = (Exception) intent.getSerializableExtra(EXCEPTION);

        if (exception == null){
            delegate.manageGetUpComingMoviesSuccess((List<MovieGridItemVO>) intent.getSerializableExtra(GET_UPCOMING_MOVIES_RESPONSE));
        } else {
            delegate.managerGetUpComingMoviesCallbackError(exception);
        }
    }

    /**
     * Registers a new broadcast receiver.
     *
     * @param delegate
     * @return GetCityWeatherForecastReceiver
     */
    public static GetUpcomingMoviesReceiver registerObserver(GetUpcomingMoviesDelegate delegate){

        GetUpcomingMoviesReceiver receiver = null;

        try {

            receiver = new GetUpcomingMoviesReceiver();
            receiver.delegate = delegate;
            LocalBroadcastManager.getInstance(delegate.retrieveApplication()).registerReceiver(receiver, new IntentFilter(GET_UPCOMING_MOVIES_OBSERVER));

        } catch(Throwable ex) {
            Log.e(TAG, "Error on register observer [" + GET_UPCOMING_MOVIES_OBSERVER + "]", ex);
        }

        return receiver;
    }

    /**
     * Unregister a broadcast receiver that was previously registered.
     *
     * @param application
     */
    public void unregisterObserver(Application application){

        try {

            LocalBroadcastManager.getInstance(application).unregisterReceiver(this);

        } catch(Throwable ex) {
            Log.e(TAG, "Error on unregister observer [" + GET_UPCOMING_MOVIES_OBSERVER + "]", ex);
        }

    }

    /**
     * Notify a registered observer that exist new data
     *
     * @param context
     * @param result
     */
    public static void notifyNewParametersReceived(Context context, List<MovieGridItemVO>  result){

        try {

            Intent intent = new Intent(GET_UPCOMING_MOVIES_OBSERVER);
            intent.putExtra(GET_UPCOMING_MOVIES_RESPONSE, (Serializable) result);

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        } catch(Throwable ex) {
            Log.e(TAG, "Error on notify new data to observer [" + GET_UPCOMING_MOVIES_OBSERVER + "]", ex);
        }
    }

    /**
     * Notify a registered observer that occurred an error
     *
     * @param context
     * @param e
     */
    public static void notifyErrorReceived(Context context, Exception e){

        try {

            Intent intent = new Intent(GET_UPCOMING_MOVIES_OBSERVER);
            intent.putExtra(EXCEPTION, e);

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        } catch(Throwable ex) {
            Log.e(TAG, "Error on notify an error to observer [" + GET_UPCOMING_MOVIES_OBSERVER + "]", ex);
        }
    }
}
