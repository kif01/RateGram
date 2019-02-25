package com.home.khalil.nosedive;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import java.util.HashMap;
import java.util.Map;

import qiu.niorgai.StatusBarCompat;


public class SplashScreen extends AwesomeSplash{



    String userID;
    double newRate;
    int totalNum;
    int iRated;
   // private FirebaseFirestore mFirestore;
    User u;
    //private FirebaseFirestore mFirestore2;
    //Map<String, Object> map;
    private FirebaseAuth mAuth;

    @Override
    public void initSplash(ConfigSplash configSplash) {

       // RatingActivity.ra.finish();
        StatusBarCompat.translucentStatusBar(this);
        Log.d("SPLASH","Splashhh");
       // Bundle bundle= getIntent().getExtras();
      //userID= bundle.getString("UID");
        //newRate= bundle.getDouble("newRate");
        //totalNum= bundle.getInt("totalNum");
        //iRated= bundle.getInt("totalIRated");
      // float totalNum= bundle.getFloat("totalNum");

      // map = new HashMap<>();
        //map.put("rating", newRate);
        //map.put("ratedMe", totalNum);

       // mAuth=FirebaseAuth.getInstance();


        //mFirestore= FirebaseFirestore.getInstance();
        //mFirestore2= FirebaseFirestore.getInstance();
       // mFirestore.collection("Users").document(userID).update(map);

      /* mFirestore.collection("Users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                 u= documentSnapshot.toObject(User.class);
                u.setRating(newRate);
                u.setRatedMe(totalNum);
                Log.d("USER_SPLASH",u.print());
             //   mFirestore2.collection("Users").document(userID).set(u);


            }
        });*/

     /*   mFirestore.collection("Users").document(userID).update("rating",newRate,"ratedMe",2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //finish();
            }
        });*/

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_emoji); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
       // configSplash.setPathSplash(SyncStateContract.Constants.); //set path String
        //configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        //configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.colorAccent); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.colorAccent); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("New User Created! ");

        configSplash.setTitleTextColor(R.color.colorAccent);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(2000);
        configSplash.setAnimTitleTechnique(Techniques.FadeIn);
       // configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {
       // User u = new User("hey","hey",newRate,null, false,0,0);
      //  mFirestore2.collection("Users").document("24vGHT2DAmdSF0EeDj9cdrzBnl23").set(u);
       // mFirestore2.collection("Users").document(userID).set(u);
       // DocumentReference docRef = mFirestore2.collection("Users").document(userID);
        //docRef.add
        //docRef.update(map);

      // mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).update("iRated",iRated);





    }




}
