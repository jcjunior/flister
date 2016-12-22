package br.com.flister.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import br.com.flister.model.Movie;
import br.com.flister.view.adapter.MovieAdapter;

/**
 * Created by junior on 22/12/2016.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME    = "flister.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Movie, Integer> movieDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
       try {
           Log.i(TAG, "Creating tables");
           TableUtils.createTable(connectionSource, Movie.class);
        } catch (SQLException e) {
           Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(TAG, "Upgrading tables");
            TableUtils.dropTable(connectionSource, Movie.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /*Movie*/

    public Dao<Movie, Integer> getMovieDAO() throws SQLException {
        if (movieDAO == null) {
            movieDAO = getDao(Movie.class);
        }
        return movieDAO;
    }

    @Override
    public void close() {
        movieDAO = null;
        super.close();
    }
}
