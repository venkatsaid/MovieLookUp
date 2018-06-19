package com.example.user.movielookup;
//Async Task for MovieDetails,Trailers and Reviews
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MyAsyncTask extends AsyncTask<String, Void, Void>{

    private final String RESULTS="results",ID="id",ORIGINAL_TITLE="original_title",OVERVIEW="overview",POSTER_PATH="poster_path",BACKDROP_PATH="backdrop_path",RELEASE_DATE="release_date",VOTE_AVERAGE="vote_average",POPULARITY="popularity",NAME="name",KEY="key",CONTENT="content",AUTHOR="author",URL="url";
    private String data="";
    private ArrayList<Movie> movies=new ArrayList<Movie>();
    private ArrayList<Trailer>Trailers=new ArrayList<>();
    private ArrayList<Reviews>Reviews=new ArrayList<>();
    MyAdapter adapterObj;
    TrailerReviewAdapter trailerReviewAdapter;
    private ProgressDialog progressDialog;
    Context ct;
    int url_check;
    public MyAsyncTask(Context context,int id) {
        this.ct=context;
        this.url_check=id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ct);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        final String img_p_url=ct.getString(R.string.image_poster_path),img_b_url=ct.getString(R.string.image_backdrop_path);
        try {
            if (url_check==1){//to get movieDetails data
            data = NetworkUtilities.GetJson(strings);
            JSONObject JO = null;
            JO = new JSONObject(data);
            JSONArray JA = (JO.getJSONArray(RESULTS));
            for (int i = 0; i < JA.length(); i++) {
                JSONObject movieObj = JA.getJSONObject(i);
                Movie movie = new Movie();
                movie.id = movieObj.getInt(ID);
                movie.display_name = movieObj.getString(ORIGINAL_TITLE);
                movie.overview = movieObj.getString(OVERVIEW);
                movie.poster_url = img_p_url+ movieObj.getString(POSTER_PATH);
                movie.backdrop_url=img_b_url+ movieObj.getString(BACKDROP_PATH);
                movie.released_date = movieObj.getString(RELEASE_DATE);
                movie.rating = (float) movieObj.getDouble(VOTE_AVERAGE);
                movie.popularity = movieObj.getDouble(POPULARITY);
                movies.add(movie);}
            }
            else if (url_check==2){//to get trailers data
                data = NetworkUtilities.GetJson(strings);
                JSONObject JO = null;
                JO = new JSONObject(data);
                JSONArray JA = (JO.getJSONArray(RESULTS));
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject movieObj = JA.getJSONObject(i);
                    Trailer trailer = new Trailer();
                    trailer.name = movieObj.getString(NAME);
                    trailer.key=(ct.getString(R.string.youtube_path)+movieObj.getString(KEY));
                    Trailers.add(trailer);}
            }
            else if (url_check==3){//to get reviews data
                data = NetworkUtilities.GetJson(strings);
                JSONObject JO = null;
                JO = new JSONObject(data);
                JSONArray JA = (JO.getJSONArray(RESULTS));
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject movieObj = JA.getJSONObject(i);
                    Reviews reviews = new Reviews();
                    reviews.author = movieObj.getString(AUTHOR);
                    reviews.content=movieObj.getString(CONTENT);
                    reviews.url=movieObj.getString(URL);
                    Reviews.add(reviews);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        if (url_check==1) {
            progressDialog.dismiss();
            if (ct.getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                MainActivity.recyclerView.setLayoutManager(new GridLayoutManager(ct, 2));
            } else {
                MainActivity.recyclerView.setLayoutManager(new GridLayoutManager(ct, 3));
            }
            adapterObj = new MyAdapter((MainActivity) ct, movies);
            MainActivity.recyclerView.setAdapter(adapterObj);
        }
        else if (url_check==2){
            progressDialog.dismiss();
            if (!Trailers.isEmpty()) {
                Main2Activity.trailer_recyler.setLayoutManager(new LinearLayoutManager(ct));
                trailerReviewAdapter = new TrailerReviewAdapter((Main2Activity) ct, Trailers, Reviews, 10);
                Main2Activity.trailer_recyler.setAdapter(trailerReviewAdapter);
            }
            else {
                Main2Activity.trailer_tv.setText(R.string.No_Trailers);
            }
        }
        else if (url_check==3){
            progressDialog.dismiss();
            if (!Reviews.isEmpty()){
                Main2Activity.review_recyler.setLayoutManager(new LinearLayoutManager(ct));
                trailerReviewAdapter =new TrailerReviewAdapter((Main2Activity) ct,Trailers,Reviews,11);
                Main2Activity.review_recyler.setAdapter(trailerReviewAdapter);}
            else {
                Main2Activity.review_tv.setText(R.string.No_Reviews);
            }
        }

    }

}
