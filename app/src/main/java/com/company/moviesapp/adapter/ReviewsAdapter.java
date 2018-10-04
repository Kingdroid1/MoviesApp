package com.company.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.moviesapp.R;
import com.company.moviesapp.model.ReviewResult;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter <ReviewsAdapter.MyViewHolder> {
    private Context mContext;
    private List <ReviewResult> reviewResults;

    public ReviewsAdapter(Context mContext, List <ReviewResult> reviewResults) {
        this.mContext = mContext;
        this.reviewResults = reviewResults;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.review_card, viewGroup, false );
        return new MyViewHolder ( view );

    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int i) {
        viewHolder.title.setText ( reviewResults.get ( i ).getAuthor () );
        String url = reviewResults.get ( i ).getUrl ();
        viewHolder.url.setText ( url );
        Linkify.addLinks ( viewHolder.url, Linkify.WEB_URLS );

    }

    @Override
    public int getItemCount() {
        return reviewResults.size ();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, url;

        public MyViewHolder(View view) {
            super ( view );
            title = view.findViewById ( R.id.review_author );
            url = view.findViewById ( R.id.review_link );

            view.setOnClickListener ( new View.OnClickListener () {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition ();

                }

            } );
        }
    }

}
