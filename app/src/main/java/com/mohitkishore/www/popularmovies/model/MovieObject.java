package com.mohitkishore.www.popularmovies.model;

import android.util.Log;

import com.mohitkishore.www.popularmovies.View.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AirUnknown on 16-07-14.
 */
public class MovieObject {

    //Static Variables
    public static String BASE_URL = "http://image.tmdb.org/t/p/w780/";
    private String LOG_TAG = MovieObject.class.getSimpleName();

    private String mOriginalTitle;
    private String mMoviePosterUrl;
    private String mOverview;
    private double mVoteAverage;
    private String mDate;
    private JSONObject mJSONObject;


    public MovieObject(JSONObject movieObject) {
        try {
            mOriginalTitle = movieObject.getString(MainActivity.ORIGINAL_TITLE);
            mOverview = movieObject.getString(MainActivity.OVERVIEW);
            mVoteAverage = movieObject.getDouble(MainActivity.VOTE_AVERAGE);
            mDate = movieObject.getString(MainActivity.RELEASE_DATE);
            mMoviePosterUrl = BASE_URL + movieObject.getString(MainActivity.BACKDROP_PATH);
            mJSONObject = movieObject;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();

        }

    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getMoviePosterUrl() {
        return mMoviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        mMoviePosterUrl = moviePosterUrl;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public JSONObject getJSONObject() {
        return mJSONObject;
    }

    public void setJSONObject(JSONObject JSONObject) {
        mJSONObject = JSONObject;
    }

    @Override
    public String toString() {
        return getJSONObject().toString();
    }
}
