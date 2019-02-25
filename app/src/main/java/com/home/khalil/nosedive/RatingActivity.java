package com.home.khalil.nosedive;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.hujiaweibujidao.wava.Techniques;
import com.github.hujiaweibujidao.wava.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingActivity extends AppCompatActivity {

   // private FirebaseFirestore mFirestoreRead;
    private FirebaseFirestore mFirestore;


    String imageUri;
    String name;
    double rating;
    boolean isFilled;
    int totalNum;
    int iRated;
    double currentRate;
    String userID;
    String currentUserName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("userID");


        Log.d("WEIRD","WHYYYYYY?");
        final TextView currentUserName_tv= (TextView) findViewById(R.id.rating_current_user_name);
        mAuth = FirebaseAuth.getInstance();
        //mFirestoreRead= FirebaseFirestore.getInstance();
        mFirestore= FirebaseFirestore.getInstance();

        TextView otherUser= (TextView) findViewById(R.id.rating_other_user_name);

        mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                currentUserName= (String) documentSnapshot.get("name");
                if (currentUserName.contains(" ")) {
                    String s = currentUserName.substring(0, currentUserName.indexOf(" "));
                    currentUserName_tv.setText(s);
                }else {
                    currentUserName_tv.setText(currentUserName);
                }

                iRated=((Long) documentSnapshot.get("iRated")).intValue();
                Log.d("IRATED",iRated+"");

            }
        });

         //name= bundle.getString("name");
        // imageUri= bundle.getString("imageUri");


        //String currentUserName= bundle.getString("currentUserName");
       // ra=this;





        final TextView otherUserName_tv=(TextView) findViewById(R.id.rating_other_user_name);
        final CircleImageView circleImageView= (CircleImageView) findViewById(R.id.rating_image);
        final RatingBar ratingBar= (RatingBar) findViewById(R.id.rating_bar);








        Log.d("UserID","Rating: "+userID);

        DocumentReference docRef = mFirestore.collection("Users").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name=(String) documentSnapshot.get("name");
                imageUri =(String) documentSnapshot.get("imageUri");
                rating= (double) documentSnapshot.get("rating");
                totalNum= ((Long) documentSnapshot.get("ratedMe")).intValue();
                isFilled = (boolean) documentSnapshot.get("isProfilefilled");
                if(isFilled == true){
                    if (name.contains(" ")){
                        String s= name.substring(0,name.indexOf(" "));
                        otherUserName_tv.setText(s);
                    }else {
                        otherUserName_tv.setText(name);
                    }
                }
                Picasso.get().load(imageUri).placeholder(R.drawable.default_profile_pic).
                        into(circleImageView);
            }
        });

        /*mFirestoreRead.collection("Users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                name=(String) documentSnapshot.get("name");
                imageUri =(String) documentSnapshot.get("imageUri");
                rating= (double) documentSnapshot.get("rating");
                totalNum= ((Long) documentSnapshot.get("ratedMe")).intValue();





                if (name.contains(" ")){
                    String s= name.substring(0,name.indexOf(" "));
                    otherUserName_tv.setText(s);
                }else {
                    otherUserName_tv.setText(name);
                }
                Picasso.get().load(imageUri).placeholder(R.drawable.default_profile_pic).into(circleImageView);





            }
        });*/






       // Log.d("THISR",currentRate+"");




        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, float v, boolean bool) {
                totalNum++;
                iRated++;
                Log.d("IRATED",iRated+"");
                final double rate= ratingBar.getRating();
                double newRate;
                final Bundle b = new Bundle();
                b.putString("UID",userID);
                b.putInt("totalNum",totalNum);

                if(isFilled==true){
                   newRate= (rate+rating)/2;
                }else{
                    newRate=rate;
                }
                Map<String,Object> map = new HashMap<>();
                map.put("rating", newRate);
                map.put("ratedMe", totalNum);
                mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).
                        update("iRated",iRated);
                mFirestore.collection("Users").document(userID).update(map);






              YoYo.with(Techniques.TakingOff).duration(1000).interpolate(new AccelerateDecelerateInterpolator()).listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Intent i;
                       // totalNum++;
                      //  iRated++;
                        if (isFilled==false){

                            i = new Intent(RatingActivity.this,SplashScreen.class);
                          //  b.putDouble("newRate", rate);
                           // i.putExtras(b);
                            startActivity(i);
                            finish();
                        }else{
                            i = new Intent(RatingActivity.this,UserProfileActivity.class);
                           // double newRate= (rate+rating)/2;
                            Bundle b= new Bundle();
                            b.putString("UID", userID);
                            i.putExtras(b);
                            startActivity(i);
                            finish();
                        }



                    }


                }).playOn(ratingBar);






            }


        });
    }


}
