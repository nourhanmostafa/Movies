package com.example.nourhan.movies;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nourhan on 9/2/2016.
 */
public class ListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Trailer> t = new ArrayList<Trailer>();

    public ListAdapter(Context context) {
        this.context = context;
    }

    public void adapter(ArrayList<Trailer> trailers) {
        t =trailers;
    }
    @Override
    public String getItem(int position) {
        return t.get(position).getKey();
    }

    @Override
    public int getCount() {
        return t.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getData(int position) {
        return t.get(position).getName();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageButton imageButton;
        TextView textView = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.trailer_item, null);
            imageButton = (ImageButton) convertView.findViewById(R.id.trailer_item1);
            textView=(TextView)convertView.findViewById(R.id.trailer_item2);
        } else {
            RelativeLayout relativeLayout=(RelativeLayout)convertView;
        }
        textView=(TextView)convertView.findViewById(R.id.trailer_item2);
        imageButton = (ImageButton) convertView.findViewById(R.id.trailer_item1);
        textView.setText(getData(position));
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(("http://www.youtube.com/watch?v=").concat(getItem(position)))));
            }
        });
        return convertView;
    }
}
