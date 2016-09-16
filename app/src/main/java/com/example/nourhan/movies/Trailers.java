package com.example.nourhan.movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
 * Created by Nourhan on 9/6/2016.
 */
public class Trailers extends AsyncTask<String,Void,ArrayList<Trailer>> {
    DetailActivity de;
    Context context;
    View rootView;
    LayoutInflater inflater;
    ViewGroup container;
    ListAdapter listAdapter;
    ArrayList<Trailer> trailers=new ArrayList<Trailer>();
    private final String LOG_TAG = Trailers.class.getSimpleName();
    Trailer trailer;

    public Trailers(Context context,View rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        android.widget.ListAdapter listAdapter=listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = ListView.MeasureSpec.makeMeasureSpec(listView.getWidth(), ListView.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, ListView.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    public ArrayList<Trailer> getMovieTrailers(String Trailers) throws JSONException {
        final String RESULTS = "results";
        final String ID="id";
        final String KEY="key";
        final String NAME="name";

        JSONObject trailersJson = new JSONObject(Trailers);
        JSONArray trailersArray = trailersJson.getJSONArray(RESULTS);

        for (int i=0;i<trailersArray.length();i++){
            JSONObject trailerData = trailersArray.getJSONObject(i);
            String id=trailerData.getString(ID);
            String key=trailerData.getString(KEY);
            String name=trailerData.getString(NAME);
            trailer=new Trailer(id,key,name);
            trailers.add(trailer);
        }
return trailers;
        //Log.d(LOG_TAG,trailers.toString());
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader trailerReader = null;
            String trailerJsonStr = null;

        try {
            Uri builtUri = null;
            String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String  BASE_URL=MOVIES_BASE_URL.concat(params[0].concat("/videos?"));
            final String APPID_PARAM = "api_key";
            //trailers
            builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, "c3bb9ee57492adbd0b982c29b7174c7d")
                    .build();

            //built url
            URL url = new URL(builtUri.toString());

            //Log.d(LOG_TAG, url.toString());

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
            trailerReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = trailerReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            trailerJsonStr = buffer.toString();
            // Log.d(LOG_TAG, trailerJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (trailerReader != null) {
                try {
                    trailerReader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
          return getMovieTrailers(trailerJsonStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    protected void onPostExecute(ArrayList<Trailer>Trailers) {
        ListView listView=(ListView)rootView.findViewById(R.id.movie_trailers_list);
        listAdapter=new ListAdapter(context);
        listAdapter.adapter(Trailers);
        listAdapter.notifyDataSetChanged();
        listView.setAdapter(listAdapter);
        setListViewHeightBasedOnChildren(listView);
    }
}
