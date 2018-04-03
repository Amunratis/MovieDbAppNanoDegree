package com.example.sirth.moviedbappnanodegree.dataModel;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class MovieDetails implements Parcelable  {

    public int getVote_count() {
        return vote_count;
    }
    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public boolean isVideo() {
        return video;
    }
    public void setVideo(boolean video) {
        this.video = video;
    }
    public float getVote_average() {
        return vote_average;
    }
    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public float getPopularity() {
        return popularity;
    }
    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }
    public String getPoster_path() {
    return "https://image.tmdb.org/t/p/w500"+ poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getBackdrop_path() {
        return "https://image.tmdb.org/t/p/w500" + backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("genre_ids")
    private List<Integer> genre_ids = new ArrayList<Integer>();
    @SerializedName("id")
    private int id;
    @SerializedName("original_title")
    private String original_title;
    @SerializedName("original_language")
    private String original_language;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("popularity")
    private float popularity;
    @SerializedName("vote_count")
    private Integer vote_count;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private float vote_average;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(vote_count);
        dest.writeInt(id);
        dest.writeByte(video ? (byte) 1 : (byte) 0);
        dest.writeFloat(vote_average);
        dest.writeString(title);
        dest.writeFloat(popularity);
        dest.writeString(poster_path);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeList(genre_ids);
        dest.writeString(backdrop_path);
        dest.writeValue(adult);
        dest.writeString(overview);
        dest.writeString(release_date);
    }

    public MovieDetails() {
    }

    MovieDetails(Parcel in) {
        setVote_count(in.readInt());
        setId(in.readInt());
        setVideo(video = in.readByte() != 0);
        setVote_average(in.readFloat());
        setTitle(in.readString());
        setPopularity(in.readFloat());
        setPoster_path(in.readString()) ;
        setOriginal_language(in.readString());
        setOriginal_title(in.readString());
        setGenre_ids(new ArrayList<Integer>()) ;
        in.readList(this.genre_ids, Integer.class.getClassLoader());
        setBackdrop_path(in.readString()) ;
        setAdult((Boolean) in.readValue(Boolean.class.getClassLoader()));
        setOverview(in.readString()) ;
        setRelease_date(in.readString()) ;
    }

    public static final Parcelable.Creator<MovieDetails>
            CREATOR = new Parcelable.Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel source) {
            return new MovieDetails(source);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };
}
