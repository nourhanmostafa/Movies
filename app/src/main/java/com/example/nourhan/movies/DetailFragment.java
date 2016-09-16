package com.example.nourhan.movies;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nourhan.movies.Detail;
import com.example.nourhan.movies.R;
import com.example.nourhan.movies.Reviews;
import com.example.nourhan.movies.Trailers;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DetailFragment extends Fragment {
    private ImageView imageView;
    private TextView movie_overview;
    private TextView vote_average;
    private TextView release_date;
    private TextView movie_title;
    private ImageButton favourite;
    Reviews reviews;
    Trailers trailers;
    Set<String> s=new HashSet<String>();
    Set<String>hs=new HashSet<String>();
    public DetailFragment(){

    }
    private void saveStae(boolean isFavourite) {
        SharedPreferences aSharedPreferenes = getContext().getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferenesEdit = aSharedPreferenes
                .edit();
        aSharedPreferenesEdit.putBoolean("State", isFavourite);
        aSharedPreferenesEdit.commit();
    }

    private boolean readStae() {
        SharedPreferences aSharedPreferenes =getContext().getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        return aSharedPreferenes.getBoolean("State", true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView1 = inflater.inflate(R.layout.detail_layout,container, false);
        //Intent intent =getActivity().getIntent();
        //final Detail d = intent.getParcelableExtra("details");
        final Detail d=getArguments().getParcelable("details");
        imageView = (ImageView) rootView1.findViewById(R.id.image_view);
        Picasso.with(imageView.getContext()).load(d.getUrl()).into(imageView);
        movie_title = (TextView)rootView1.findViewById(R.id.movie_title);
        movie_title.setText(d.getTitle());
        release_date = (TextView) rootView1.findViewById(R.id.release_date);
        release_date.setText(d.getReleaseDate());
        vote_average = (TextView) rootView1.findViewById(R.id.vote_average);
        vote_average.setText(d.getVoteAverage());
        movie_overview = (TextView) rootView1.findViewById(R.id.movie_overview);
        movie_overview.setText(d.getOverview());
        reviews=new Reviews(getContext(),rootView1);
        reviews.execute(d.getId());
        trailers=new Trailers(getContext(),rootView1);
        trailers.execute(d.getId());
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                "Fav", Context.MODE_PRIVATE);
        favourite=(ImageButton)rootView1.findViewById(R.id.favourite);
        s=sharedPref.getStringSet("set",new HashSet<String>());
        final Gson gson = new Gson();
        //  String contain=gson.toJson(d);
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            String json= (String) iter.next();
            Detail detail=gson.fromJson(json,Detail.class);
            if(d.getId().equals(detail.getId())){
                saveStae(true);
                favourite.setBackgroundResource(R.drawable.favourite1);
                break;
            }
        }

        favourite.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {
                boolean isFavourite = readStae();
                SharedPreferences sharedPref = getContext().getSharedPreferences(
                        "Fav", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String json = gson.toJson(d);

                if (isFavourite) {
                    favourite.setBackgroundResource(R.drawable.notfav);
                    isFavourite = false;
                    saveStae(isFavourite);
                    hs=sharedPref.getStringSet("set",new HashSet<String>());
                    hs.remove(json);
                    editor.remove("set");
                    editor.commit();
                    editor.putStringSet("set",hs);
                    editor.commit();
                } else {
                    favourite.setBackgroundResource(R.drawable.favourite1);
                    isFavourite = true;
                    saveStae(isFavourite);
                    hs=sharedPref.getStringSet("set",new HashSet<String>());
                    hs.add(json);
                    editor.remove("set");
                    editor.commit();
                    editor.putStringSet("set",hs);
                    editor.commit();
                }

            }
        });
        return rootView1;
    }
}