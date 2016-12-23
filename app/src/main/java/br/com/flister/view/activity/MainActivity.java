package br.com.flister.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import br.com.flister.R;
import br.com.flister.utils.DataOrigin;
import br.com.flister.view.fragment.MoviesGridFragment;
import br.com.flister.view.fragment.MoviesGridFragment_;

/**
 * Created by jcjunior on 19/12/2016.
 */
@EActivity
@OptionsMenu(R.menu.toolbar_movie)
public class MainActivity extends AppCompatActivity  {

    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById
    BottomBar bottomBar;

    @ViewById(R.id.my_toolbar)
    Toolbar myToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(myToolbar);

        //BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_recent);
        //nearby.setBadgeCount(5);
        //nearby.removeBadge();
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_favorites:
                        callMoviesGridFragment(DataOrigin.DATA_BASE);
                        break;
                    case R.id.tab_movies:
                        callMoviesGridFragment(DataOrigin.REST_API);
                        break;
                    case R.id.tab_recent:
                        callMoviesGridFragment(DataOrigin.SHARED_PREFERENCES);
                        break;
                }
            }
        });

    }

    private void callMoviesGridFragment(DataOrigin dataOrigin) {
        MoviesGridFragment moviesGridFragment = MoviesGridFragment_.builder()
                .dataOrigin(dataOrigin)
                .build();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.myScrollingContent, moviesGridFragment)
                .commit();

        Log.d(TAG, "MainActivity called MoviesGridFragment ["+dataOrigin+"] with success ");
    }

    @Override
    public void onBackPressed() {
        // disable going back to the LoginActivity
        moveTaskToBack(true);
    }

    @OptionsItem(R.id.action_search)
    void searchCity(MenuItem item) {

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Toast.makeText(MainActivity.this, query, Toast.LENGTH_LONG).show();

                Log.d(TAG, "WeatherListActivity called CityWeatherFragment with success");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @OptionsItem(R.id.action_logout)
    void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure that you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = LoginActivity_.intent(MainActivity.this).get();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.create().show();


    }


}
