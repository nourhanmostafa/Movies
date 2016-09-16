package com.example.nourhan.movies;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.gson.Gson;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Nourhan on 8/11/2016.
 */
public class MoviesFragment extends Fragment {
    ArrayList<Detail> urls = new ArrayList<Detail>();
    GridViewAdapter gridViewAdapter;
    MovieListener movieListener;


    public MoviesFragment() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onResume() {
        Set<String>h=new HashSet<String>();
        ArrayList<Detail> fav=new ArrayList<Detail>();
        super.onResume();
        ApiConnection ApiTask = new ApiConnection();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String display = prefs.getString(getString(R.string.pref_display_key), getString(R.string.pref_display_default));
        if(display.equals("favourites")){
            SharedPreferences sharedPref = getContext().getSharedPreferences(
                    "Fav", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            h=sharedPref.getStringSet("set",new HashSet<String>());
            Iterator iter = h.iterator();
            while (iter.hasNext()) {
                String json= (String) iter.next();
                Detail d=gson.fromJson(json,Detail.class);
                fav.add(d);
            }
            if(movieListener.getPane()&&fav.size()>0){
                movieListener.setSelectedMovie(fav.get(0));}
            gridViewAdapter.adapter(fav);
            gridViewAdapter.notifyDataSetChanged();
        }
       else{
            ApiTask.execute(display);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_fragment, menu);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.grid_view,container, false);
        GridView grid = (GridView) rootView.findViewById(R.id.gridview);
        gridViewAdapter = new GridViewAdapter(getActivity());
        gridViewAdapter.notifyDataSetChanged();
        grid.setAdapter(gridViewAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Detail movies = (Detail) gridViewAdapter.getData(position);
                movieListener.setSelectedMovie(movies);
            }
        });
        return rootView;

    }

    public void setMovieListener(MovieListener mListener){
        movieListener=mListener;
    }


    public class ApiConnection extends AsyncTask<String, Void, ArrayList<Detail>> {
        private final String LOG_TAG = ApiConnection.class.getSimpleName();
        Detail detail;
        private ArrayList<Detail> getMoviePath(String moviesData)
                throws JSONException {
            final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
            final String SIZE = "w185";
            final String RESULTS = "results";
            final String PATH = "poster_path";
            final String TITLE = "original_title";
            final String OVERVIEW = "overview";
            final String VOTE_AVERAGE = "vote_average";
            final String RELEASE_DATE = "release_date";
            final String ID="id";

            JSONObject moviesJson = new JSONObject(moviesData);
            JSONArray moviesArray = moviesJson.getJSONArray(RESULTS);
            urls.clear();
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieData = moviesArray.getJSONObject(i);

                String path = movieData.getString(PATH);
                String title = movieData.getString(TITLE);
                String overview = movieData.getString(OVERVIEW);
                String voteAverage = movieData.getString(VOTE_AVERAGE);
                String releaseDate = movieData.getString(RELEASE_DATE);
                String id=movieData.getString(ID);
                String url = POSTER_BASE_URL.concat(SIZE).concat(path);
                detail = new Detail(title, overview, voteAverage, releaseDate, url,id);
                urls.add(detail);
            }
            return urls;
        }


        @Override
        protected ArrayList<Detail> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;
            try {
                Uri builtUri = null;
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
                final String MOVIES_BASE_URL1 = "http://api.themoviedb.org/3/movie/top_rated?";
                final String APPID_PARAM = "api_key";

             if (params[0].equals("popular")) {
                    builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                            .appendQueryParameter(APPID_PARAM, "c3bb9ee57492adbd0b982c29b7174c7d")
                            .build();
                } else if(params[0].equals("topRated")){
                    builtUri = Uri.parse(MOVIES_BASE_URL1).buildUpon()
                            .appendQueryParameter(APPID_PARAM, "c3bb9ee57492adbd0b982c29b7174c7d")
                            .build();
                }
                //built url
                URL url = new URL(builtUri.toString());

                //create the request and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //read data into String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                // if no data
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMoviePath(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(ArrayList<Detail> urls) {
            super.onPostExecute(urls);
            gridViewAdapter.adapter(urls);
            gridViewAdapter.notifyDataSetChanged();
            if(movieListener.getPane()){
                movieListener.setSelectedMovie(urls.get(0));}
        }

    }


}
