package com.example.sirth.moviedbappnanodegree.view.RecyclerAdapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sirth.moviedbappnanodegree.R;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieTrailer;
import com.example.sirth.moviedbappnanodegree.networkUtil.NetworkConstants;

import java.util.List;

/**
 * Created by sirth on 01/04/2018.
 */

public class TrailersRecAdapter extends RecyclerView.Adapter<TrailersRecAdapter.ViewHolder> {

    private Context context;
    private List<MovieTrailer.MovieVideos> movieTrailer;

    public TrailersRecAdapter(Context context,List <MovieTrailer.MovieVideos> movieTrailer) {
        this.context = context;
        this.movieTrailer = movieTrailer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer,
                parent,false);
        return new ViewHolder(view);



    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Glide.with(context)
                .load(movieTrailer.get(position).getSite())
                .placeholder(R.drawable.ic_play_arrow_black_84dp)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return movieTrailer.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
               imageView=itemView.findViewById(R.id.trailer);

               itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       int position=getAdapterPosition();

                       Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + movieTrailer.get(position).getKey()));
                       Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkConstants.YOUTUBE_URL + movieTrailer.get(position).getKey()));

                       //If youtube isn't installed then use webIntent
                       try {
                           context.startActivity(youtubeIntent);
                       } catch (ActivityNotFoundException e) {
                           context.startActivity(webIntent);
                       }

                   }
               });



        }
    }
}
