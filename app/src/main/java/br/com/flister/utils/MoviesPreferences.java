package br.com.flister.utils;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.DefaultStringSet;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

import java.util.Set;

/**
 * Created by junior on 22/12/2016.
 */

@SharedPref(SharedPref.Scope.ACTIVITY)
public interface MoviesPreferences {

    /**
     * Movies recently visited
     * @return
     */
    @DefaultStringSet( value = "")
    Set<String> movies_ids();
}
