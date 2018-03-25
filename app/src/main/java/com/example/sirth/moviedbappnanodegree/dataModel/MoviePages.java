package com.example.sirth.moviedbappnanodegree.dataModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sirth on 19/03/2018.
 */

public class MoviePages {


    private int page;
    private List<MovieDetails> results;
    private int total_results;
    private int total_Pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieDetails> getResults() {
        return results;
    }

    public void setResults(List<MovieDetails> results) {
        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(int total_Pages) {
        this.total_Pages = total_Pages;
    }
}
