package com.example.nourhan.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.GridView;

import static com.example.nourhan.movies.MoviesFragment.*;

public class MainActivity extends AppCompatActivity implements MovieListener{
boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout container1=(FrameLayout)findViewById(R.id.container1);
        if(null==container1){
        mTwoPane=false;
        }
        else{
            mTwoPane=true;
        }
        if (savedInstanceState == null) {
            MoviesFragment moviesFragment=new MoviesFragment();
            moviesFragment.setMovieListener(this);
            getSupportFragmentManager().beginTransaction().add(R.id.container, moviesFragment).commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
  public boolean getPane(){
    return mTwoPane;
   }
    @Override
    public void setSelectedMovie(Detail movie) {
        //two pane UI
        if(mTwoPane){
            DetailFragment detailFragment=new DetailFragment();
            Bundle extras=new Bundle();
            extras.putParcelable("details",movie);
            detailFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.container1,detailFragment).commit();
        }

        //single pane UI
        else{
            Intent intent = new Intent(this,DetailActivity.class).putExtra("details", movie);
            startActivity(intent);
        }

    }


}
