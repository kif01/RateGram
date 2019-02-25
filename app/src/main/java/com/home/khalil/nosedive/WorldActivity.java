package com.home.khalil.nosedive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import qiu.niorgai.StatusBarCompat;

public class WorldActivity extends AppCompatActivity {

    private RecyclerView postsRCV;
    private ArrayList<Post> postsList;
    private PostsRecycleViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);
        //StatusBarCompat.translucentStatusBar(this);

        postsRCV= (RecyclerView) findViewById(R.id.main_recycler_view);

        postsList= new ArrayList<>();

        Post p1= new Post("Khalil Faraj",4.75,"https://firebasestorage.googleapis.com/v0/b/nosedive-a730d.appspot.com/o/UsersProfilePics%2Fc1VsjNVT8JSU8pyQ2UXt0rgWiHH2?alt=media&token=b5079261-e531-4db4-b420-43593d1bec10","Had an amazing day today!",R.drawable.sunset,0);
        Post p2= new Post("Angela Martin",4.53,"https://dz9yg0snnohlc.cloudfront.net/new-the-enjoyable-art-of-learning-to-talk-to-random-people-2.png","Just watched Deadpool 2. So Funnyy! Great Movie.",R.drawable.deadpool,0);
        Post p5= new Post("Justin LaVine",3.51,"https://pbs.twimg.com/media/BduTxWnIUAAKT_5.jpg:large","So in for a partayy!!.",0,0);
        Post p3= new Post("Nicky Romero",4.95,"http://images4.fanpop.com/image/photos/16300000/Random-people-random-16382026-375-500.jpg","I would love a vacation right now.",0,0);
        Post p4= new Post("John Newman",3.92,"https://pbs.twimg.com/media/BcINeMVCIAABeWd.jpg","Curry is the King.",R.drawable.nba,0);
        Post p6= new Post("McDonald's",4.7,"http://www.radiokerry.ie/wp-content/uploads/sites/16/McDonalds-logo-300x300.png","The Big Mac!!",R.drawable.bigmac,0);
        //Post p1= new Post("Khalil Faraj",4.75,,"Had an amazing day today!",R.drawable.sunset,0);
        //Post p1= new Post("Khalil Faraj",4.75,,"Had an amazing day today!",R.drawable.sunset,0);
        postsList.add(p1);
        postsList.add(p3);
        postsList.add(p2);
        postsList.add(p5);
        postsList.add(p6);
        postsList.add(p4);
        mAdapter= new PostsRecycleViewAdapter(postsList);
        postsRCV.setHasFixedSize(true);
        postsRCV.setLayoutManager(new LinearLayoutManager(this));
        postsRCV.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}
