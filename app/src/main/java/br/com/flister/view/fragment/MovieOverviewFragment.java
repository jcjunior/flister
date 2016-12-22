package br.com.flister.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import br.com.flister.R;
import br.com.flister.model.MovieGridItemVO;
import br.com.flister.utils.Constants;

/**
 * Created by junior on 21/12/2016.
 */

@EFragment(R.layout.fragment_movie_overview)
public class MovieOverviewFragment extends Fragment {

    @ViewById(R.id.txt_movie_overview)
    TextView movieOverview;

    @FragmentArg(Constants.MOVIE_SELECTED)
    MovieGridItemVO movieSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void onCreateView(){

        if (movieSelected != null){
            movieOverview.setText(movieSelected.getOverview());
        }

    }
}
