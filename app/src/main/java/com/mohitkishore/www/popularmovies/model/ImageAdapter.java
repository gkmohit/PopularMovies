package com.mohitkishore.www.popularmovies.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AirUnknown on 16-07-12.
 */
public class ImageAdapter extends BaseAdapter {

    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();
    public static ArrayList<MovieObject> mMovieObject;
    private Context mContext;

    public ImageAdapter(Context context) {
        mContext = context;
    }

    public ImageAdapter(Context context, ArrayList<MovieObject> movieObject) {
        mMovieObject = movieObject;
        mContext = context;
    }

    public ArrayList<MovieObject> getMovieObject() {
        return mMovieObject;
    }

    public void setMovieObject(ArrayList<MovieObject> movieObject) {
        mMovieObject = movieObject;
    }

    public int getCount() {
        return getMovieObject().size();
    }

    public MovieObject getItem(int position) {
        return mMovieObject.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(mMovieObject.get(position).getMoviePosterUrl()).into(imageView);
        return imageView;
    }
}

