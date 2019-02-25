package com.home.khalil.nosedive;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.hujiaweibujidao.wava.Techniques;
import com.github.hujiaweibujidao.wava.YoYo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        StatusBarCompat.translucentStatusBar(this);

        FloatingActionButton home= (FloatingActionButton) findViewById(R.id.go_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Bundle bundle= getIntent().getExtras();
        userID= bundle.getString("UID");

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore=FirebaseFirestore.getInstance();
        mFirestore.setFirestoreSettings(settings);


        final CircleImageView profileImage= (CircleImageView) findViewById(R.id.profile_image);
        final TextView rate1= (TextView) findViewById(R.id.profile_rating1) ;
        final TextView rate2= (TextView) findViewById(R.id.profile_rating2) ;
        final TextView name= (TextView) findViewById(R.id.profile_name) ;
        final TextView bio= (TextView) findViewById(R.id.profile_bio) ;
        final TextView iRated= (TextView) findViewById(R.id.profile_user_rate) ;
        final TextView ratedMe= (TextView) findViewById(R.id.profile_rated_user) ;

        mFirestore.collection("Users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                User u = documentSnapshot.toObject(User.class);
                name.setText(u.getName());
                bio.setText(u.getBio());
                iRated.setText(u.getiRated()+"");

                Log.d("TEST",u.print());
                ratedMe.setText(u.getRatedMe()+"");
                double r = u.getRating();
                String s= String.valueOf(r);
                String r1;
                String r2;
                if(s.length()>=5) {
                    r1 = s.substring(0, 3);
                    r2 = s.substring(3, 5);
                }else if(s.length()==4){
                    r1 = s.substring(0, 3);
                    r2=s.charAt(3)+"0";
                }else{
                    r1=s;
                    r2="00";
                }
                rate1.setText(r1);
                rate2.setText(r2);
                Picasso.get().load(u.getImageUri()).placeholder(R.drawable.default_profile_pic).into(profileImage);
            }
        });


        YoYo.with(Techniques.FadeInUp).duration(1800).playOn(rate1);
        YoYo.with(Techniques.FadeInUp).duration(1800).playOn(rate2);
    }
}
