package com.example.user.movielookup;
//Movie details adapter class
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    Context ct;
    ArrayList<Movie> movie = new ArrayList<>();
    public MyAdapter(MainActivity context, ArrayList<Movie> list) {
        this.ct=context;
        this.movie=list;
    }

   @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        
        View v1=LayoutInflater.from(ct).inflate(R.layout.mainlist,parent,false);
        return new MyViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(ct).load(movie.get(position).poster_url).error(R.mipmap.no_image).into(holder.images);
    }
    @Override
    public int getItemCount() {
        return (movie == null) ? 0 : movie.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        public MyViewHolder(View itemView) {
            super(itemView);
            images=itemView.findViewById(R.id.image1);
            images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> movieinfo=new ArrayList<>();
                    int position=getLayoutPosition();
                    movieinfo.add(0,movie.get(position).backdrop_url);
                    movieinfo.add(1,movie.get(position).poster_url);
                    movieinfo.add(2,movie.get(position).display_name);
                    movieinfo.add(3,movie.get(position).released_date);
                    movieinfo.add(4,movie.get(position).overview);
                    movieinfo.add(5,Float.toString(movie.get(position).rating));
                    movieinfo.add(6, String.valueOf(movie.get(position).id));
                    Intent intent=new Intent(ct,Main2Activity.class);
                    intent.putStringArrayListExtra("Movie info",movieinfo);
                    v.getContext().startActivity(intent);
                }
            });
            }
    }
}
