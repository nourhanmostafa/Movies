package com.example.nourhan.movies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nourhan on 8/8/2016.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    MovieListener movieListener;
    private ArrayList<Detail> p = new ArrayList<Detail>();
    private final String LOG_TAG = MoviesFragment.class.getSimpleName();

    public GridViewAdapter(Context context) {
        this.context = context;
    }

    public void adapter(ArrayList<Detail> poster) {
        p = poster;
    }

    @Override
    public int getCount() {
        return p.size();
    }

    @Override
    public String getItem(int position) {
        return p.get(position).getUrl();
    }

    public Object getData(int position) {
        return p.get(position);
    }
/*public ArrayList<Detail>getList(){
    return p;
}*/
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_view_item, null);
            imageView = (ImageView) convertView.findViewById(R.id.image);

        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(context).load(getItem(position)).into(imageView);

        return imageView;
    }
}
