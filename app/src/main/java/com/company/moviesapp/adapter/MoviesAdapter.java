package com.company.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.moviesapp.DetailActivity;
import com.company.moviesapp.R;
import com.company.moviesapp.model.Movies;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{

    private Context mContext;
    private List<Movies> moviesList;

    public MoviesAdapter(Context mContext, List <Movies> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from ( parent.getContext () )
                .inflate ( R.layout.movie_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.ViewHolder holder, int i) {
        holder.title.setText ( moviesList.get ( i ).getOriginalTitle () );
        String vote = Double.toString ( moviesList.get ( i ).getVoteAverage () );
        holder.userrating.setText ( vote );

        String poster = "https://image.tmdb.org/t/p/w500" + moviesList.get ( i ).getPosterPath ();

        Picasso.get ()
                .load ( poster )
                .placeholder ( R.drawable.user_placeholder )
                .error ( R.drawable.error_placeholder )
                .into ( holder.thumbnail );
    }

    public void setMovies(List <Movies> movies) {
        moviesList = movies;
        notifyDataSetChanged ();
    }

    @Override
    public int getItemCount() {
        return moviesList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView ( R.id.title ) TextView title;
        @BindView ( R.id.user_rating ) TextView userrating;
        @BindView ( R.id.thumbnail ) ImageView thumbnail;


        public ViewHolder(View view){
            super (view);
            ButterKnife.bind ( this, view );

            view.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition ();
                    if (pos != RecyclerView.NO_POSITION){
                        Movies clickedDataItem = moviesList.get ( pos );
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra ( "movies", clickedDataItem );
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity ( intent );
                        Toast.makeText (v.getContext (), "You clicked" + clickedDataItem.getOriginalTitle (), Toast.LENGTH_SHORT).show ();
                    }
                }
            } );
        }
    }
}
