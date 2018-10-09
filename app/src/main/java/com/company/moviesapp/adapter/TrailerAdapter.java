package com.company.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.moviesapp.R;
import com.company.moviesapp.model.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter <TrailerAdapter.ViewHolder> {

    private Context context;
    private List <Trailer> trailerList;

    public TrailerAdapter(Context context, List <Trailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from ( parent.getContext () )
                .inflate ( R.layout.trailer_card, parent, false );
        return new ViewHolder ( view );
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText ( trailerList.get ( i ).getName () );
    }

    @Override
    public int getItemCount() {
        return trailerList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public ViewHolder(View itemView) {
            super ( itemView );
            title = itemView.findViewById ( R.id.title );
            thumbnail = itemView.findViewById ( R.id.thumbnail );

            itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition ();
                    if (pos != RecyclerView.NO_POSITION) {
                        Trailer clickedDataItem = trailerList.get ( pos );
                        String videoId = trailerList.get ( pos ).getKey ();
                        Intent intent = new Intent ( Intent.ACTION_VIEW, Uri.parse ( "vnd.youtube:" + videoId ) );
                        intent.putExtra ( "Video_ID", videoId );
                        context.startActivity ( intent );
                        Toast.makeText ( v.getContext (), "You clicked " + clickedDataItem.getName (), Toast.LENGTH_SHORT ).show ();
                    }
                }
            } );
        }
    }
}
