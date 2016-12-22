package br.com.flister.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.flister.R;

/**
 * Created by junior on 21/12/2016.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    public TextView title, releaseDate;
    public ImageView thumbnail, overflow;

    public MovieViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        releaseDate = (TextView) view.findViewById(R.id.releaseDate);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        overflow = (ImageView) view.findViewById(R.id.overflow);
    }

}
