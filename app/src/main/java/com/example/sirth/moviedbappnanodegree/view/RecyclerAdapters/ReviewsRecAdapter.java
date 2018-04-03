package com.example.sirth.moviedbappnanodegree.view.RecyclerAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sirth.moviedbappnanodegree.R;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieReview;

import java.util.List;

/**
 * Created by sirth on 01/04/2018.
 */

public class ReviewsRecAdapter extends RecyclerView.Adapter<ReviewsRecAdapter.ViewHolder> {


    Context context;
    List<MovieReview.MovieReviews> movieReviewsList;

    public ReviewsRecAdapter(Context context, List<MovieReview.MovieReviews> movieReviewsList) {
        this.context = context;
        this.movieReviewsList = movieReviewsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_review,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(movieReviewsList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return movieReviewsList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;


        public ViewHolder(View view){
            super(view);
            textView=view.findViewById(R.id.reviews);

        }
    }



}
