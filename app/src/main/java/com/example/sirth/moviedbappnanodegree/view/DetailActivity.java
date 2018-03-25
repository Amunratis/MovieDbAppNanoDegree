package com.example.sirth.moviedbappnanodegree.view;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sirth.moviedbappnanodegree.R;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieDetails;
import com.example.sirth.moviedbappnanodegree.dataModel.MoviePages;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MovieDetails movieDetails=(MovieDetails)getIntent().getParcelableExtra("movies");
        ImageView imageView=(ImageView)findViewById(R.id.image);

        Glide.with(getApplicationContext())
                .load(movieDetails.getPoster_path())
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);

    }

}
