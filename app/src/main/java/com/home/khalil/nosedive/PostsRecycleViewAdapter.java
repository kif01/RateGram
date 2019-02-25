package com.home.khalil.nosedive;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by khalil on 8/17/18.
 */

public class PostsRecycleViewAdapter extends RecyclerView.Adapter<PostsRecycleViewAdapter.ViewHolder> {

    public ArrayList<Post> postsList;



    public PostsRecycleViewAdapter(ArrayList<Post> postsList) {
        this.postsList=postsList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name=postsList.get(position).getName();
        double rating= postsList.get(position).getRating();
        String imageUri= postsList.get(position).getImageUri();
        int image= postsList.get(position).getImage();
        String text= postsList.get(position).getText();
        double postRating= postsList.get(position).getPostRating();



        holder.nameText.setText(name);
        holder.textText.setText(text);
        holder.userRatingText.setText(rating+"");
        if(image !=0){
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(image);
        }else{
            holder.imageView.setVisibility(View.GONE);
        }

            Picasso.get().load(imageUri).placeholder(R.drawable.default_profile_pic).into(holder.profileImage);

        }








    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView nameText;
        public TextView textText;
        public TextView userRatingText;
        public ImageView imageView;
        public TextView postRatingText;
        public CircleImageView profileImage;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            nameText= (TextView) mView.findViewById(R.id.post_user_name);
            textText= (TextView) mView.findViewById(R.id.post_text);
            profileImage = (CircleImageView) mView.findViewById(R.id.post_user_pic);
            userRatingText= (TextView) mView.findViewById(R.id.post_user_rate);
            ratingBar= (RatingBar) mView.findViewById(R.id.post_rating);
            imageView= (ImageView) mView.findViewById(R.id.post_image);


        }
    }
}
