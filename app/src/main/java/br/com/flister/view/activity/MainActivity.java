package br.com.flister.view.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Switch;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.flister.R;
import br.com.flister.view.fragment.MoviesGridFragment;
import br.com.flister.view.fragment.MoviesGridFragment_;

/**
 * Created by jcjunior on 19/12/2016.
 */
@EActivity
public class MainActivity extends AppCompatActivity  {

    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById
    BottomBar bottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_recent);
        //nearby.setBadgeCount(5);
        //nearby.removeBadge();
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_favorites:
                        break;
                    case R.id.tab_movies:
                        callMoviesGridFragment();
                        break;
                    case R.id.tab_recent:
                        break;
                }
            }
        });

    }

    private void callMoviesGridFragment() {
        MoviesGridFragment moviesGridFragment = MoviesGridFragment_.builder().build();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.myScrollingContent, moviesGridFragment)
                .commit();

        Log.d(TAG, "MainActivity called MoviesGridFragment with success");
    }


}
