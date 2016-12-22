package br.com.flister.view.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import br.com.flister.R;
import br.com.flister.model.MovieGridItemVO;

/**
 * Created by junior on 21/12/2016.
 */

@EBean
public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<MovieGridItemVO> movies;

    @RootContext
    Context context;

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {

        MovieGridItemVO movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.subtitle.setText(movie.getSubtitle());

        Glide.with(context).load(movie.getPoster()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, MovieGridItemVO movie) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_movie, popup.getMenu());
        popup.setOnMenuItemClickListener(new MovieAdapter.MyMenuItemClickListener(movie));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private MovieGridItemVO movie;

        public MyMenuItemClickListener(MovieGridItemVO movie) {
            this.movie = movie;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favorite:
                    Toast.makeText(context, "Add to favorite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_overview:
                    Toast.makeText(context, movie.getOverview(), Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }

    }

    public void setMovies(List<MovieGridItemVO> movies) {
        this.movies = movies;
    }
}
