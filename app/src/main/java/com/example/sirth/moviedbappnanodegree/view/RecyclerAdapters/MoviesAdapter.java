package com.example.sirth.moviedbappnanodegree.view.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sirth.moviedbappnanodegree.R;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieDetails;
import com.example.sirth.moviedbappnanodegree.view.DetailActivity;


import java.util.List;

/**
 * Created by sirth on 13/03/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.Movies> {

    private Context mContext;
    private List<MovieDetails> movieDetailsList;

    public MoviesAdapter(Context mContext, List<MovieDetails> movieDetailsList){
        this.mContext = mContext;
        this.movieDetailsList = movieDetailsList;
    }


    @Override
    public MoviesAdapter.Movies onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,
                parent,false);

        return new Movies(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.Movies holder, int position) {

            Glide.with(mContext)
                    .load(movieDetailsList.get(position).getPoster_path())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imageView);


    }

    @Override
    public int getItemCount() {return movieDetailsList.size();
    }

    public class Movies extends RecyclerView.ViewHolder{
        ImageView imageView;

        public Movies(View view){
         super(view);
        imageView=view.findViewById(R.id.poster);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int posit=getAdapterPosition();

                if(posit!=RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MovieDetails movieDetails = movieDetailsList.get(getAdapterPosition());
                    intent.putExtra("parcel", movieDetails);
                    String string =movieDetails.getPoster_path();
                    Toast.makeText(mContext,string,Toast.LENGTH_SHORT).show();
///asfadsfd


                }
            }
        });
        }
    }
}
