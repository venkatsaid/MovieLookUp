package com.example.user.movielookup;
//Adapter for Trailers and Reviews
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TrailerReviewAdapter extends RecyclerView.Adapter<TrailerReviewAdapter.TrailerViewHolder> {

    Context ct;
    int id;
    ArrayList<Trailer> trailer=new ArrayList<>();
    ArrayList<Reviews> reviews=new ArrayList<>();
    public TrailerReviewAdapter(Main2Activity context, ArrayList<Trailer> trailer, ArrayList<Reviews> reviews, int id) {
        this.ct=context;
        this.trailer=trailer;
        this.reviews=reviews;
        this.id=id;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1=null;
        if (id==10) {//for trailers
            v1 = LayoutInflater.from(ct).inflate(R.layout.trailerlist, parent, false);
        }
        else if (id==11){//for reviews
            v1 = LayoutInflater.from(ct).inflate(R.layout.reviewslist, parent, false);
        }
        return new TrailerViewHolder(v1);

    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, final int position) {
        if (id==10) {//for reviews
            holder.trailerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(trailer.get(position).key));
                    ct.startActivity(viewIntent);
                }
            });
            holder.trailerName.setText(trailer.get(position).name);
        }
        else if (id==11){//for reviews
            holder.author.setText(reviews.get(position).author);
            holder.content.setText(reviews.get(position).content);
            holder.url.setText(reviews.get(position).url);
        }
    }

    @Override
    public int getItemCount() {
        int count=0;
        if (id==10){ count=trailer.size();}//for trailers
        else if (id==11){ count=reviews.size();}//for reviews
        return count;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView trailerImage;
        TextView trailerName,author,url,content;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            if (id==10){//for trailers
                trailerImage= (ImageView) itemView.findViewById(R.id.button);
                trailerName=(TextView) itemView.findViewById(R.id.trailername);}
            else if (id==11){//for  Reviews
                author=itemView.findViewById(R.id.action_author);
                content=itemView.findViewById(R.id.action_content);
                url=itemView.findViewById(R.id.action_url);
            }

        }
    }
}

