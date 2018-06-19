package com.example.user.movielookup;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.user.movielookup.Contract.movie_backdroppath;
import static com.example.user.movielookup.Contract.movie_id;
import static com.example.user.movielookup.Contract.movie_overview;
import static com.example.user.movielookup.Contract.movie_posterpath;
import static com.example.user.movielookup.Contract.movie_rating;
import static com.example.user.movielookup.Contract.movie_releasedate;
import static com.example.user.movielookup.Contract.movie_title;
import static com.example.user.movielookup.MainActivity.apiKey;


public class Main2Activity extends AppCompatActivity {

    private ArrayList<String> moviedetails=new ArrayList<>();
    @BindView(R.id.title) TextView title;
    @BindView(R.id.releaseDate) TextView date;
    @BindView(R.id.ratingTextView) TextView rating;
    @BindView(R.id.overview) TextView overview;
    @BindView(R.id.rating) RatingBar ratingBar;
    @BindView(R.id.posterImageView) ImageView poster;
    @BindView(R.id.backdrop) ImageView backdrop;
    public static RecyclerView trailer_recyler;
    public static RecyclerView review_recyler;
    boolean check_fav=false;
    static TextView trailer_tv,review_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        trailer_recyler=findViewById(R.id.trailer_recyler);
        review_recyler=findViewById(R.id.review_recycler);
        trailer_tv=findViewById(R.id.trailer_tv);
        review_tv=findViewById(R.id.review_tv);
        final TextView favInfo=findViewById(R.id.about_fav);
        final ImageView favorites=findViewById(R.id.fav_icon);
        moviedetails=getIntent().getStringArrayListExtra("Movie info");//Taking data from adapter
        Picasso.with(this).load(moviedetails.get(0)).error(R.mipmap.no_image).into(backdrop);
        Picasso.with(this).load(moviedetails.get(1)).error(R.mipmap.no_image).into(poster);
        title.setText("Title:"+"\""+moviedetails.get(2)+"\"");
        date.setText("Release Date:"+moviedetails.get(3));
        rating.setText(moviedetails.get(5)+"/10");
        overview.setText(moviedetails.get(4));
        ratingBar.setRating(Float.parseFloat(moviedetails.get(5)));
        
        Cursor cursor=getContentResolver().query(Uri.parse(Contract.CONTENT_URI+"/*"),null, Contract.movie_title+" = ?;",new String[]{moviedetails.get(2)},null);
        check_fav=cursor.getCount() > 0;
        if (check_fav){//Conditon to check if movie is already a favorite or not
            favInfo.setText("Favorite");
            favorites.setImageResource(R.mipmap.fav);
        }
        else {
            favInfo.setText("Not Favorite");
            favorites.setImageResource(R.mipmap.unfav);
        }
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//action when clicked on heart or favorite symbol
                if (check_fav) {//if movie is already in favorite delete it from storage
                    getContentResolver().delete(Uri.parse(Contract.CONTENT_URI + "/*"), Contract.movie_title + " = ?;",new  String[]{moviedetails.get(2)});
                    check_fav=false;
                    favInfo.setText("Not Favorite");
                    favorites.setImageResource(R.mipmap.unfav);
                }
                else {//if movie not in favorite add to storage

                    ContentValues movieDetailValues = new ContentValues();
                    movieDetailValues.put(movie_id, Integer.parseInt(moviedetails.get(6)));
                    movieDetailValues.put(movie_title, moviedetails.get(2));
                    movieDetailValues.put(movie_backdroppath, moviedetails.get(0));
                    movieDetailValues.put(movie_posterpath, moviedetails.get(1));
                    movieDetailValues.put(movie_rating, Float.parseFloat(moviedetails.get(5)));
                    movieDetailValues.put(movie_releasedate, moviedetails.get(3));
                    movieDetailValues.put(movie_overview, moviedetails.get(4));
                    getContentResolver().insert(Uri.parse(Contract.CONTENT_URI+""), movieDetailValues);
                    check_fav=true;
                    favInfo.setText("Favorite");
                    favorites.setImageResource(R.mipmap.fav);

                }

            }
        });
        //Async Task usage for getting trailer info
        MyAsyncTask a = new MyAsyncTask(this, 2);
        a.execute(getString(R.string.movieDbBaseUrl) + moviedetails.get(6) + getString(R.string.video_path)+apiKey);
        //Async Task usage for getting reviews info
        MyAsyncTask b = new MyAsyncTask(this,  3);
        b.execute(getString(R.string.movieDbBaseUrl) + moviedetails.get(6) + getString(R.string.review_path)+apiKey);

    }
}