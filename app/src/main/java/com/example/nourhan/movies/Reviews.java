package com.example.nourhan.movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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

/**
 * Created by Nourhan on 9/5/2016.
 */
    public class Reviews extends AsyncTask<String,Void,String> {
    private final String LOG_TAG = Reviews.class.getSimpleName();
        DetailActivity de;
        Context context;
        View rootView;
        LayoutInflater inflater;
        ViewGroup container;
        private ArrayAdapter<String> itemsAdapter;
 public Reviews(Context context, View rootView) {
     this.context = context;
     this.rootView = rootView;
 }

    public String getMovieReviews(String Reviews) throws JSONException {
        final String RESULTS = "results";
        final String AUTHOR="author";
        final String CONTENT="content";
        JSONObject reviewsJson = new JSONObject(Reviews);
        JSONArray reviewsArray = reviewsJson.getJSONArray(RESULTS);
        String review="";
        for (int i=0;i<reviewsArray.length();i++){
            JSONObject reviewData=reviewsArray.getJSONObject(i);
            String author=reviewData.getString(AUTHOR);
            String content=reviewData.getString(CONTENT);
             review+=("Author/").concat(author).concat(":\n").concat(content).concat("\n").concat("\n");

            }
            return review;
        }


        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection1 = null;
            BufferedReader reviewsReader= null;
            String reviewsJsonStr = null;


            //reviews
            try {

                Uri builtUri1 = null;
                String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String BASE_URL1 =MOVIES_BASE_URL.concat(params[0].concat("/reviews?"));
                final String APPID_PARAM = "api_key";

                //reviews
                builtUri1 = Uri.parse(BASE_URL1).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "c3bb9ee57492adbd0b982c29b7174c7d")
                        .build();

                //built url
                URL url1 = new URL(builtUri1.toString());

                //Log.d(LOG_TAG, url.toString());

                //create the request and open the connection
                urlConnection1 = (HttpURLConnection) url1.openConnection();
                urlConnection1.setRequestMethod("GET");
                urlConnection1.connect();

                //read data into String
                InputStream inputStream1 = urlConnection1.getInputStream();
                StringBuffer buffer1 = new StringBuffer();

                // if no data
                if (inputStream1 == null) {
                    return null;
                }
                reviewsReader = new BufferedReader(new InputStreamReader(inputStream1));
                String line1;

                while ((line1 = reviewsReader.readLine()) != null) {
                    buffer1.append(line1 + "\n");
                }
                if (buffer1.length() == 0) {
                    return null;
                }
                reviewsJsonStr = buffer1.toString();
                //Log.d(LOG_TAG, reviewsJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection1 != null) {
                    urlConnection1.disconnect();
                }
                if (reviewsReader != null) {
                    try {
                        reviewsReader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieReviews(reviewsJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String Reviews) {
            TextView textView =(TextView)rootView.findViewById(R.id.review_item1);
            textView.setText(Reviews);
        }

    }


