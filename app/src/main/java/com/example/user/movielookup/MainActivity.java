package com.example.user.movielookup;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String p_url,r_url;
    static String apiKey = BuildConfig.API_KEY;
    static RecyclerView recyclerView;
    SwipeRefreshLayout mySwipeRefreshLayout;
    final String ONSAVEINSTANCE="instance";
    String instanceValue="popular";
    DbHelper datahelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p_url=getResources().getString(R.string.popular_url)+apiKey;
        r_url=getResources().getString(R.string.top_rating_url)+apiKey;
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler);
        if(isOnline()){
            if (savedInstanceState!=null){
                if(savedInstanceState.containsKey(ONSAVEINSTANCE)){
                    switch (savedInstanceState.getString(ONSAVEINSTANCE)){
                        case "popular":
                            instanceValue="popular";
                            vs(instanceValue);
                            break;
                        case "top_rated":
                            instanceValue="top_rated";
                            vs(instanceValue);
                            break;
                        case "favorites":
                            instanceValue="favorites";
                            vs(instanceValue);
                            break;
                    }
                }
                else {
                    vs(instanceValue);
                }
            }
            else{
                vs(instanceValue);
            }

        }else{
            //If there is no network available then to display favorites
            instanceValue="favorites";
            vs("favorites");
        }
        //swipe refresh layout usage to refresh the application
        mySwipeRefreshLayout=findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        finish();
                        Intent i=new Intent(MainActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                }
        );

    }
    //method to check internet connection
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

//Menu items usage
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }


    public void clickedPopular(MenuItem item) {
        instanceValue="popular";
        vs(instanceValue);
    }

    public void clickedHighestRated(MenuItem item) {
      instanceValue="top_rated";
       vs(instanceValue);
    }
    public void clickedfavorite(MenuItem item) {
       instanceValue="favorites";
       vs(instanceValue);
    }
    //saved instances
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ONSAVEINSTANCE,instanceValue);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        vs(instanceValue);
    }
    //A method that calls async task according to operation
    public void vs(String val){
        switch (val){
            case "popular":
                if(isOnline()){
                MyAsyncTask a=new MyAsyncTask(this,1);
                a.execute(p_url);
                }
                else {
                    Toast.makeText(this,"Check Network\nSwipe To Refresh", Toast.LENGTH_LONG).show();
                }
                break;
            case "top_rated":
                if (isOnline()){
                MyAsyncTask b=new MyAsyncTask(this,1);
                b.execute(r_url);
                }
                else {
                    Toast.makeText(this,"Check Network\nSwipe To Refresh", Toast.LENGTH_LONG).show();
                }
                break;
            case "favorites":
                datahelper=new DbHelper(this);
                Cursor favcursor=getContentResolver().query(Uri.parse(Contract.CONTENT_URI+""),null,null,null,null);
                ArrayList<Movie> moviedbinfo=new ArrayList<>();
                if (favcursor.getCount()>=0) {

                    if (favcursor.moveToFirst()) {
                        do {
                            Movie movieinfo = new Movie();
                            movieinfo.id = favcursor.getInt(1);
                            movieinfo.display_name = favcursor.getString(2);
                            movieinfo.backdrop_url = favcursor.getString(3);
                            movieinfo.poster_url = favcursor.getString(4);
                            movieinfo.rating = (float) favcursor.getDouble(5);
                            movieinfo.released_date = favcursor.getString(6);
                            movieinfo.overview = favcursor.getString(7);
                            moviedbinfo.add(movieinfo);
                        } while (favcursor.moveToNext());
                    }
                    favcursor.close();
                    MyAdapter favadapter;
                    if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        MainActivity.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                    } else {
                        MainActivity.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                    }
                    favadapter=new MyAdapter(MainActivity.this,moviedbinfo);
                    recyclerView.setAdapter(favadapter);

                }
                //popular movies called when no favourites selected
                if (favcursor.getCount()==0){
                    instanceValue="popular";
                    vs(instanceValue);
                    Toast.makeText(this, getString(R.string.no_fav_added), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}