package com.home.khalil.nosedive;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.kairos.Kairos;
import com.kairos.KairosListener;
import qiu.niorgai.StatusBarCompat;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import eu.amirs.JSON;

public class WelcomeActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 100;
    private static final int MY_CAMERA_PERMISSION_CODE = 0;
    private FirebaseFirestore mFirestore;
    //FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    final String PASS= "wasabi";
    final String GMAIL="@gmail.com";
    String galleryId = "users";
    String app_id = "abdf65f3";
    String api_key = "8aedb3480325fd88402b184b795f44ff";

    Kairos myKairos;
    KairosListener listener;
    KairosListener enrollListener;
    private Bitmap photo;
    Intent i;


    String uuid;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private AnimationDrawable anim;
    private KairosListener delete;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_welcome);
        StatusBarCompat.translucentStatusBar(this);

        FloatingActionButton fabStart=  (FloatingActionButton) findViewById(R.id.fab_start);
        mAuth = FirebaseAuth.getInstance();

        mFirestore=FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);


        RelativeLayout container= (RelativeLayout) findViewById(R.id.container);

        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(6000);
        anim.setExitFadeDuration(2000);





        myKairos = new Kairos();
        myKairos.setAuthentication(this, app_id, api_key);




        mProgressDialog= new ProgressDialog(this,R.style.MyAlertDialogStyle);

        mProgressDialog.setMessage("Logging in...");




        fabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                  /*   try {
                         myKairos.deleteGallery(galleryId, delete);
                     } catch (JSONException e) {
                         e.printStackTrace();
                     } catch (UnsupportedEncodingException e) {
                         e.printStackTrace();
                     }*/

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });

        delete= new KairosListener() {
            @Override
            public void onSuccess(String s) {
                Log.d("KAIROS DEMO", "Deleted:"+s);
            }

            @Override
            public void onFail(String s) {
                Log.d("KAIROS DEMO",s);
            }
        };

       /* try {
            myKairos.deleteGallery(galleryId, delete);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


        enrollListener= new KairosListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("KAIROS DEMO", "Done:"+response);
                Intent i= new Intent(WelcomeActivity.this, FirstTimeActivity.class);
                Bundle bundle= new Bundle();
                bundle.putParcelable("image",photo);
                i.putExtras(bundle);
                startActivity(i);finish();
            }
            @Override
            public void onFail(String s) {
            }
        };

        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("KAIROS DEMO", response);
                if (response.contains("no match found") ||
                        response.contains("gallery name not found")) {
                    Intent i= new Intent(WelcomeActivity.this,
                            FirstTimeActivity.class);
                    Bundle bundle= new Bundle();
                    bundle.putParcelable("image",photo);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();

                    Log.d("Here", "true");

                }else if (response.contains("Error")){
                    mProgressDialog.hide();
                    Toast.makeText(WelcomeActivity.this,
                            "No face was detected. Try again. ",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("blabla","success");
                    JSON json = new JSON(response);
                    String id=json.key("images").index(0).key("candidates")
                            .index(0).key("subject_id").stringValue();
                    Log.d("kd", id);
                        String[] parts = id.split("_");
                        String idPart2 = parts[1];
                    signIn(idPart2+GMAIL,PASS);
                }
            }

            @Override
            public void onFail(String response) {
                Log.d("KAIROS DEMO", "Here: "+response);
            }
        };



        //DELETE GALLERY


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
              //  cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            try {
                mProgressDialog.show();
                myKairos.recognize(photo, galleryId, null, null,
                        null, null, listener);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void createAccount(final String email, String password) {
        //Log.d(TAG, "createAccount:" + email);

        // showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                             userID= user.getUid();
                            Log.d("uid",userID);
                           /* try {
                                myKairos.enroll(photo, userID+"_"+uuid, galleryId, null, null, null, enrollListener);;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }*/

                            Intent i= new Intent(WelcomeActivity.this, FirstTimeActivity.class);
                            Bundle bundle= new Bundle();
                            bundle.putParcelable("image",photo);
                          //  bundle.putString("subjectID",userID+"_"+uuid);
                            i.putExtras(bundle);
                            startActivity(i);
                            finish();




                            //updateUI(user);
                        }else {
                            Toast.makeText(WelcomeActivity.this, "Authentication failed",
                                    Toast.LENGTH_LONG).show();
                        }



                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                        mProgressDialog.hide();
                        mProgressDialog.dismiss();
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(final String email, final String pass){
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.hide();
                            mProgressDialog.dismiss();
                            Log.d("signIn", "signInWithEmail:success "+
                                    mAuth.getCurrentUser().getUid());
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            checkFirstTime(currentUser);
                        } else {
                            mProgressDialog.hide();
                            mProgressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w("signIn", "signInWithEmail:failure",
                                    task.getException());

                            Toast.makeText(WelcomeActivity.this,
                                    "Authentication Failed."+ "  "+email,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
         /*   Log.d("ssp",SaveSharedPreference.getPref(WelcomeActivity.this));
            //checkFirstTime(currentUser);
            if(SaveSharedPreference.getPref(WelcomeActivity.this).length() == 0){
                startActivity(new Intent(WelcomeActivity.this, FirstTimeActivity.class));
                finish();
            }else{
                Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }*/
         checkFirstTime(currentUser);

        }
    }

    private void checkFirstTime( FirebaseUser currentUser) {

        DocumentReference docRef = mFirestore.collection("Users").document(currentUser.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    boolean b = (boolean) documentSnapshot.get("isProfilefilled");
                    if (b) {
                        Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Log.d("Boolean", b + "");
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(WelcomeActivity.this, EditProfileActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putParcelable("image",photo);
                         i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtras(bundle);
                        startActivity(i);
                        //finish();
                    }

                } catch (NullPointerException npe) {
                    Log.d("errorx", npe.toString());
                }
            }
        });
       /* mFirestore.collection("Users").document(currentUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                try {
                    boolean b = (boolean) documentSnapshot.get("isProfilefilled");
                    if (b) {
                        Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Log.d("Boolean", b + "");
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(WelcomeActivity.this, EditProfileActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putParcelable("image",photo);
                       // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtras(bundle);
                        startActivity(i);
                        //finish();
                    }

                } catch (NullPointerException npe) {
                    Log.d("errorx", npe.toString());
                }
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    // Stopping animation:- stop the animation on onPause.
    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }
}
