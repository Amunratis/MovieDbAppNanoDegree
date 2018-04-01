package com.example.sirth.moviedbappnanodegree.view.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sirth.moviedbappnanodegree.R;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieDetails;
import com.example.sirth.moviedbappnanodegree.database.MovieSqlProvider;
import com.example.sirth.moviedbappnanodegree.database.MovieSqliteHelper;
import com.example.sirth.moviedbappnanodegree.view.DetailActivity;


import java.util.List;

/**
 * Created by sirth on 13/03/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.Movies> {

    private Context mContext;
    private List<MovieDetails> movieDetailsList;

    private Cursor mCursor;

    public MoviesAdapter(Context mContext, List<MovieDetails> movieDetailsList){
        this.mContext = mContext;
        this.movieDetailsList = movieDetailsList;
    }
    public MoviesAdapter(Context context, Cursor cursor)
    {
        this.mContext=context;
        this.mCursor=cursor;
    }


    @Override
    public MoviesAdapter.Movies onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,
                parent,false);

        return new Movies(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.Movies holder, int position) {

        if(movieDetailsList!=null) {
            Glide.with(mContext)
                    .load(movieDetailsList.get(position).getPoster_path())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imageView);
        }else
        {   mCursor.moveToPosition(position);
            Glide.with(mContext)
                .load(mCursor.getString(mCursor.getColumnIndex(MovieSqliteHelper.COLUMN_POSTER_PATH)))
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageView);}


    }

    @Override
    public int getItemCount() {
        if(movieDetailsList!=null){
        return movieDetailsList.size();}
        return mCursor.getCount();

    }

    public class Movies extends RecyclerView.ViewHolder{
        ImageView imageView;

        public Movies(View view){
         super(view);
        imageView=view.findViewById(R.id.poster);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int Position = getAdapterPosition();

                //case when getting data from Internet
                if(movieDetailsList!=null) {

                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MovieDetails movieDetails = movieDetailsList.get(getAdapterPosition());
                        intent.putExtra("parcel", movieDetails);
                        mContext.startActivity(intent);

                }else//Case when getting data from SQL
                {
                    mCursor.moveToPosition(Position);
                    Intent intent=new Intent(mContext,DetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", mCursor.getString(mCursor.getColumnIndex(MovieSqliteHelper._id)));
                            bundle.putString("poster_path", mCursor.getString
                            (mCursor.getColumnIndex(MovieSqliteHelper.COLUMN_POSTER_PATH)));
                    bundle.putString("rate", String.valueOf(mCursor.getDouble(mCursor.getColumnIndex(MovieSqliteHelper.COLUMN_USER_RATING))));
                    bundle.putString("title", mCursor.getString
                            (mCursor.getColumnIndex(MovieSqliteHelper.COLUMN_TITLE)));
                    bundle.putString("release_data", mCursor.getString
                            (mCursor.getColumnIndex(MovieSqliteHelper.COLUMN_RELEASE_DATE)));
                    bundle.putString("overview", mCursor.getString
                            (mCursor.getColumnIndex(MovieSqliteHelper.COLUMN_SYNOPSIS)));
                    bundle.putString("backdrop_path", mCursor.getString
                            (mCursor.getColumnIndex(MovieSqliteHelper.COLUMN_BACKDROP_PATH)));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

                }
            }
        });
        }
    }
}
