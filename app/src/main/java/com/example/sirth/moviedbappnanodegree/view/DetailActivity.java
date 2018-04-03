package com.example.sirth.moviedbappnanodegree.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sirth.moviedbappnanodegree.R;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieDetails;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieReview;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieTrailer;

import com.example.sirth.moviedbappnanodegree.database.MovieSqlProvider;
import com.example.sirth.moviedbappnanodegree.database.MovieSqliteHelper;
import com.example.sirth.moviedbappnanodegree.networkUtil.RetrofitClient;
import com.example.sirth.moviedbappnanodegree.networkUtil.RetrofitService;
import com.example.sirth.moviedbappnanodegree.view.RecyclerAdapters.ReviewsRecAdapter;
import com.example.sirth.moviedbappnanodegree.view.RecyclerAdapters.TrailersRecAdapter;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    TrailersRecAdapter trailersRecAdapter;
    ReviewsRecAdapter reviewsRecAdapter;
    RecyclerView trailersRecyclerView;
    RecyclerView reviewsRecyclerView;
    MovieDetails movieDetails;
    List<MovieTrailer.MovieVideos> trailerDetails;
    List<MovieReview.MovieReviews> movieReviewsDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button button=findViewById(R.id.favourites);
        movieDetails=getIntent().getParcelableExtra("parcel");

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
                getTrailers();
                getReviews();
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
    public  void  getReviews(){
        reviewsRecyclerView=findViewById(R.id.reviewsRec);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        try {
            RetrofitService service = RetrofitClient.getClient().create(RetrofitService.class);

            //TODO API KEY


            Call<MovieReview> call = service.getReviews(movieDetails.getId(),"");
            call.enqueue(new Callback<MovieReview>() {
                @Override
                public void onResponse(Call<MovieReview> call, Response<MovieReview> response) {
                    movieReviewsDetails = response.body().getResults();
                    reviewsRecAdapter=new ReviewsRecAdapter(DetailActivity.this,  movieReviewsDetails);
                    reviewsRecAdapter.notifyDataSetChanged();
                    reviewsRecyclerView.setAdapter(reviewsRecAdapter);
                }
                @Override
                public void onFailure(Call<MovieReview> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error Fetching Data!", Toast.LENGTH_LONG).show();
                }
            });
        }

        catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getTrailers(){
        trailersRecyclerView =findViewById(R.id.trailers);
        trailersRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        trailersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        try {
            RetrofitService service = RetrofitClient.getClient().create(RetrofitService.class);

            //TODO API KEY


            Call<MovieTrailer> call = service.getTrailers(movieDetails.getId(),"");
            call.enqueue(new Callback<MovieTrailer>() {
                @Override
                public void onResponse(Call<MovieTrailer> call, Response<MovieTrailer> response) {

                    trailerDetails = response.body().getResults();
                    trailersRecAdapter=new TrailersRecAdapter(DetailActivity.this, trailerDetails);
                    trailersRecAdapter.notifyDataSetChanged();
                    trailersRecyclerView.setAdapter(trailersRecAdapter);
                }
                @Override
                public void onFailure(Call<MovieTrailer> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error Fetching Data!", Toast.LENGTH_LONG).show();
                }
            });
        }

        catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

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
        if(flag==true){unfavourite();}

    }
}
