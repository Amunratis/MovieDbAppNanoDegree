package com.example.sirth.moviedbappnanodegree.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sirth.moviedbappnanodegree.R;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieDetails;
import com.example.sirth.moviedbappnanodegree.database.MovieSqlProvider;
import com.example.sirth.moviedbappnanodegree.database.MovieSqliteHelper;

import java.util.Locale;


public class DetailActivity extends AppCompatActivity {

    String ID;
    String title;
    String overview;
    String poster;
    String thumb;
    String rate;
    String release_date;
    boolean flag;
    boolean firstClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button button=findViewById(R.id.favourites);
        MovieDetails movieDetails=getIntent().getParcelableExtra("parcel");



            if (getIntent().getParcelableExtra("parcel") != null) {
                ID = String.valueOf(movieDetails.getId());
                try (Cursor cursor = getContentResolver().query(MovieSqlProvider.CONTENT_URI, null,
                        MovieSqliteHelper._id + " = " + DatabaseUtils.sqlEscapeString(ID), null, null)) {
                    if (cursor != null && cursor.getCount() != 0) {
                        button.setText(R.string.unfavourite);
                    }
                }
                 movieDetails = getIntent().getParcelableExtra("parcel");
                initViewsFromParcelable(movieDetails);
            } else {
                ID = String.valueOf(getIntent().getExtras().getString("id"));
                try (Cursor cursor = getContentResolver().query(MovieSqlProvider.CONTENT_URI, null,
                        MovieSqliteHelper._id + " = " + DatabaseUtils.sqlEscapeString(ID), null, null)) {
                    if (cursor != null && cursor.getCount() != 0) {
                        button.setText(R.string.unfavourite);
                    }
                }
                initViewsFromBundle(getIntent().getExtras());
            }

        if(button.getText().equals("Favourite")){
        button.setTag(1);}
        else button.setTag(0);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final int status =(Integer) v.getTag();
               /*if button's text was favourite change it to "unfavourite" and
               * add the movie to database or do nothing if in Favourites sort*/
                if(status==1) {
                    button.setText(R.string.unfavourite);
                    v.setTag(0);

                    //Check if it was started by favourite sort
                    if(getIntent().getExtras().getString("id")==null)
                    {   //and if it wasn't then it was started by Top/Popular so favourite it
                        //and set flag to false so it won't get unfavourited once we exit activity
                        flag=false;
                        //check flag for whether if it is first time click of the button
                        if(!firstClicked) {
                            favourite();
                        }
                        firstClicked =true;
                    }else
                    {
                        //Prevent from deleting from database until onDestroy
                        flag=false;
                    }

                }else {
                    button.setText(R.string.favourite);
                    v.setTag(1);
                    firstClicked =true;

                    /*if it wasn't started by favourite sort then just set flag to true
                    * so it will get deleted from database one activity is destroyed*/
                     flag=true;


                }

            }
        });




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void unfavourite(){
        String selection = MovieSqliteHelper._id + " = " +
                DatabaseUtils.sqlEscapeString(ID);
        getContentResolver().delete(MovieSqlProvider.CONTENT_URI, selection, null);
    }


    void favourite() {
        ContentValues contentValues = new ContentValues();

        MovieDetails movieDetails=getIntent().getParcelableExtra("parcel");
        ID=String.valueOf(movieDetails.getId());

        if (movieDetails != null) {
            contentValues.put(MovieSqliteHelper._id, ID);
            contentValues.put(MovieSqliteHelper.COLUMN_TITLE, movieDetails.getTitle());
            contentValues.put(MovieSqliteHelper.COLUMN_BACKDROP_PATH, movieDetails.getBackdrop_path());
            contentValues.put(MovieSqliteHelper.COLUMN_USER_RATING, movieDetails.getVote_average());
            contentValues.put(MovieSqliteHelper.COLUMN_POSTER_PATH, movieDetails.getPoster_path());
            contentValues.put(MovieSqliteHelper.COLUMN_SYNOPSIS, movieDetails.getOverview());
            contentValues.put(MovieSqliteHelper.COLUMN_RELEASE_DATE, movieDetails.getRelease_date());
        } else {
            contentValues.put(MovieSqliteHelper._id, ID);
            contentValues.put(MovieSqliteHelper.COLUMN_TITLE, title);
            contentValues.put(MovieSqliteHelper.COLUMN_BACKDROP_PATH, thumb);
            contentValues.put(MovieSqliteHelper.COLUMN_USER_RATING, rate);
            contentValues.put(MovieSqliteHelper.COLUMN_POSTER_PATH, poster);
            contentValues.put(MovieSqliteHelper.COLUMN_SYNOPSIS, overview);
            contentValues.put(MovieSqliteHelper.COLUMN_RELEASE_DATE, release_date);
        }
       getContentResolver().insert(MovieSqlProvider.CONTENT_URI, contentValues);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void initViewsFromParcelable(MovieDetails movieDetails){
        ImageView imageView=(ImageView)findViewById(R.id.image);

        Glide.with(getApplicationContext())
                .load(movieDetails.getPoster_path())
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);

        ImageView backdrop=findViewById(R.id.imagev);
        Glide.with(getApplicationContext())
                .load(movieDetails.getBackdrop_path())
                .placeholder(R.drawable.ic_launcher_background)
                .into(backdrop);

        TextView title=findViewById(R.id.title2);
        title.setText(movieDetails.getTitle());

        TextView released=findViewById(R.id.released);
        released.setText(movieDetails.getRelease_date());

        TextView description=findViewById(R.id.description);
        description.setText(movieDetails.getOverview());

        TextView rating=findViewById(R.id.rating);
        rating.setText(String.format(String.format(Locale.US,"%.1f",movieDetails.getVote_average())));
    }
    void initViewsFromBundle(Bundle bundle){
        ImageView imageView=(ImageView)findViewById(R.id.image);

        Glide.with(getApplicationContext())
                .load(bundle.getString("poster_path"))
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);

        ImageView backdrop=findViewById(R.id.imagev);
        Glide.with(getApplicationContext())
                .load(bundle.getString("backdrop_path"))
                .placeholder(R.drawable.ic_launcher_background)
                .into(backdrop);

        TextView title=findViewById(R.id.title2);
        title.setText(bundle.getString("title"));

        TextView released=findViewById(R.id.released);
        released.setText(bundle.getString("release_data"));

        TextView description=findViewById(R.id.description);
        description.setText(bundle.getString("overview"));

        TextView rating=findViewById(R.id.rating);
        rating.setText(bundle.getString("rate"));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"onDestroy",Toast.LENGTH_LONG).show();
        if(flag==true){unfavourite();}

    }
}
