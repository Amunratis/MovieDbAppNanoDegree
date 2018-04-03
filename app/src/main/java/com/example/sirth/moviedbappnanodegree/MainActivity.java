package com.example.sirth.moviedbappnanodegree;



import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sirth.moviedbappnanodegree.dataModel.MovieDetails;
import com.example.sirth.moviedbappnanodegree.dataModel.MoviePages;
import com.example.sirth.moviedbappnanodegree.database.MovieSqlProvider;
import com.example.sirth.moviedbappnanodegree.networkUtil.RetrofitClient;
import com.example.sirth.moviedbappnanodegree.networkUtil.RetrofitService;
import com.example.sirth.moviedbappnanodegree.view.RecyclerAdapters.MoviesAdapter;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements   LoaderManager.LoaderCallbacks<Cursor>
         {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Check whether if the Internet is turned on
        if (!checkConnection(this)) {
            Toast.makeText(this, "No connection", Toast.LENGTH_LONG).show();}
            getPopularMovies();

    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void getPopularMovies(){


        try {
            RetrofitClient Client = new RetrofitClient();
            RetrofitService service = Client.getClient().create(RetrofitService.class);
//TODO API KEY
            Call<MoviePages> call = service.getPopularMovies("");
            call.enqueue(new Callback<MoviePages>() {
                @Override
                public void onResponse(Call<MoviePages> call, Response<MoviePages> response) {
                    List<MovieDetails> details = response.body().getResults();
                    MoviesAdapter moviesAdapter=new MoviesAdapter(getApplicationContext(), details);
                    moviesAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(moviesAdapter);
                }

                @Override
                public void onFailure(Call<MoviePages> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    void getTopRatedMovies(){

        try {
            RetrofitClient Client = new RetrofitClient();
            RetrofitService service = Client.getClient().create(RetrofitService.class);

            /*TODO API KEY*/

            Call<MoviePages> call = service.getTopRated("");
            call.enqueue(new Callback<MoviePages>() {
                @Override
                public void onResponse(Call<MoviePages> call, Response<MoviePages> response) {
                    List<MovieDetails> details = response.body().getResults();
                    MoviesAdapter moviesAdapter=new MoviesAdapter(getApplicationContext(), details);
                    recyclerView.setAdapter(moviesAdapter);
                    moviesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MoviePages> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {

            final CharSequence sort[] = new CharSequence[]{"Top Rated", "Most Popular", "Favourite"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sort By:");
            builder.setItems(sort, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position
                    // of the selected item
                switch (which) {
                    case 0:
                        getPopularMovies();
                    break;
                case 1:
                    getTopRatedMovies();
                    break;
                case 2:
                    getSupportLoaderManager().initLoader(1, null,MainActivity.this);
               }
                }});
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

             @Override
             public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                 return new CursorLoader(getApplicationContext(), MovieSqlProvider.CONTENT_URI, null,
                         null, null, null);
             }



             @Override
             public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                 MoviesAdapter adapter = new MoviesAdapter(getApplicationContext(),cursor);
                 recyclerView.setAdapter(adapter);
                 adapter.notifyDataSetChanged();

             }

             @Override
             public void onLoaderReset(Loader<Cursor> loader) {

             }
         }


