package com.company.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {


    @BindView ( R.id.movie_title ) TextView nameOfMovie;
    @BindView ( R.id.plot_synopsis ) TextView plotSynopsis;
    @BindView ( R.id.user_rating ) TextView userRating;
    @BindView ( R.id.release_date ) TextView releaseDate;
    @BindView ( R.id.thumbnail_image_header ) ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_detail );
        Toolbar toolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        initCollapsingToolbar();

        ButterKnife.bind ( this );

        Intent intentThatStartedThisActivity = getIntent ();
        if (intentThatStartedThisActivity.hasExtra ("originalTitle"  )){

            String thumbnail = getIntent ().getExtras ().getString ( "posterPath" );
            String movieName = getIntent ().getExtras ().getString ( "originalTitle" );
            String synopsis = getIntent ().getExtras ().getString ( "overview" );
            String rating = getIntent ().getExtras ().getString ( "voteAverage" );
            String dateOfRelease = getIntent ().getExtras ().getString ( "releaseDate" );

            Picasso.get()
                    .load(thumbnail)
                    .noFade()
                    .error(getResources().getDrawable(R.drawable.ic_image_black))
                    .placeholder(getResources().getDrawable(R.drawable.ic_image_black))
                    .into(imageView);

            nameOfMovie.setText ( movieName );
            plotSynopsis.setText ( synopsis );
            userRating.setText ( rating );
            releaseDate.setText ( dateOfRelease );
        }else {
            Toast.makeText ( this, "No API Data", Toast.LENGTH_SHORT ).show ();
        }
    }

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById ( R.id.collapsing_toolbar );
        collapsingToolbarLayout.setTitle("");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById ( R.id.appbar );
        appBarLayout.setExpanded ( true );

        appBarLayout.addOnOffsetChangedListener ( new AppBarLayout.OnOffsetChangedListener () {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange ();
                }
                if (scrollRange + verticalOffset == 0){
                    collapsingToolbarLayout.setTitle ( getString ( R.string.mv_details ) );
                    isShow = true;
                }else if (isShow){
                    collapsingToolbarLayout.setTitle ("");
                    isShow = false;
                }
            }
        } );
    }
}

