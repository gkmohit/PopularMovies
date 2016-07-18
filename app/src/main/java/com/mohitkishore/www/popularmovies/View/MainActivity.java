package com.mohitkishore.www.popularmovies.View;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.mohitkishore.www.popularmovies.model.ImageAdapter;
import com.mohitkishore.www.popularmovies.R;
import com.mohitkishore.www.popularmovies.model.MovieObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String ORIGINAL_TITLE = "original_title";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";

    GridView mMovieGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mImageView = (ImageView) findViewById(R.id.imageView);

//        Picasso.with(getBaseContext()).load("http://i.imgur.com/DvpvklR.png").into(mImageView);
//        mMovieGridView = (GridView) findViewById(R.id.movieGridView);
//        mMovieGridView.setAdapter(new ImageAdapter(this));

        getMovies();
        mMovieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String movieTitle = ImageAdapter.mMovieObject.get(position).getOriginalTitle();

                MovieObject obj = (MovieObject) mMovieGridView.getAdapter().getItem(position);
                Log.d(LOG_TAG, obj.toString());
                Toast.makeText(getBaseContext(), movieTitle,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivtymenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            Toast.makeText(this, "Refresh", Toast.LENGTH_LONG).show();
            getMovies();
        } else if( id == R.id.sort_by) {

        }
        return true;
    }

    private void getMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        mMovieGridView = (GridView) findViewById(R.id.movieGridView);
        moviesTask.execute();

    }

    private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<MovieObject>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String RESULTS = "results";


        private ArrayList<MovieObject> getMovieObjectsFromJson(String movieJsonStr) throws JSONException {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray resultsArray = movieJson.getJSONArray(RESULTS);
            ArrayList<MovieObject> movieObjectList = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObj = (JSONObject) resultsArray.get(i);
                movieObjectList.add(new MovieObject(resultObj));
            }
            return movieObjectList;
        }

        @Override
        protected ArrayList<MovieObject> doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader;
            String movieListJSONStr;
            try {
                String API_KEY_PARAM = "api_key";
                String MOVIE_DB_POPULAR_URL = getString(R.string.movie_db_popular_url);
                String MOVIE_DB_TOP_RATED_URL = getString(R.string.movie_db_top_rated_url);
                //TODO use the settings and see what was choosen.
                String MOVIE_DB_URL;
                String MOVIE_DB_API_KEY = getString(R.string.movie_db_api_key);

                Uri builtUri = Uri.parse(MOVIE_DB_POPULAR_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, MOVIE_DB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                Log.d(LOG_TAG, builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieListJSONStr = buffer.toString();

            } catch (IOException ex) {
                Log.e(LOG_TAG, "Error ", ex);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            try {
                return getMovieObjectsFromJson(movieListJSONStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<MovieObject> movieObjects) {
            Log.d(LOG_TAG, movieObjects.toString());
            mMovieGridView.setAdapter(new ImageAdapter(MainActivity.this, movieObjects));
            super.onPostExecute(movieObjects);
        }
    }
}
