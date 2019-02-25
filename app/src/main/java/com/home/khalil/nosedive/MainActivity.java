package com.home.khalil.nosedive;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kairos.Kairos;
import com.kairos.KairosListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import eu.amirs.JSON;
import qiu.niorgai.StatusBarCompat;

public class MainActivity extends AppCompatActivity {

    Kairos myKairos;
    KairosListener listener;
    KairosListener enrollListener;
    KairosListener delete;
    private static final int CAMERA_REQUEST = 100;
    private static final int MY_CAMERA_PERMISSION_CODE = 0;
    private ImageView imageView;
    String galleryId = "users";
    String otherUserID;
    Bitmap photo;
    String test;
    String image = "http://media.kairos.com/liz.jpg";
    String email;
    String currentUserName;

    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth2;
    private FirebaseFirestore mFirestore;

    private ProgressDialog mProgressDialog;

    final String PASS= "wasabi";
    final String GMAIL="@gmail.com";
    private String uuid;
    private StorageReference mStorageReference;
    private FirebaseFirestore mFirestoreRead;
    private FirebaseFirestore mFirestoreEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        mAuth2=FirebaseAuth.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore=FirebaseFirestore.getInstance();
        mFirestore.setFirestoreSettings(settings);

        mProgressDialog= new ProgressDialog(this,R.style.MyAlertDialogStyle);

        mProgressDialog.setMessage("Creating Account...");

       final CircleImageView profileImage= (CircleImageView) findViewById(R.id.main_profile_image);
        final TextView rate1= (TextView) findViewById(R.id.main_rating1) ;
        final TextView rate2= (TextView) findViewById(R.id.main_rating2) ;
        final TextView name= (TextView) findViewById(R.id.main_name) ;
        final TextView bio= (TextView) findViewById(R.id.main_bio) ;
        final TextView iRated= (TextView) findViewById(R.id.main_i_rated) ;
        final TextView ratedMe= (TextView) findViewById(R.id.main_rated_me) ;


       /* DocumentReference currentDoc = mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid());
        currentDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                name.setText(u.getName());
                bio.setText(u.getBio());
                iRated.setText(u.getiRated()+"");
                ratedMe.setText(u.getRatedMe()+"");
                double r = u.getRating();
                String s= String.valueOf(r);
                String r1= s.substring(0,3);
                String r2= s.substring(3,5);
                rate1.setText(r1);
                rate2.setText(r2);
                Picasso.get().load(u.getImageUri()).placeholder(R.drawable.default_profile_pic).into(profileImage);

            }
        });*/

        mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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



        mStorageReference= FirebaseStorage.getInstance().getReference();

        Button photoButton = (Button) this.findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.image) ;

        email=mAuth.getCurrentUser().getEmail();
        final String uid2=mAuth.getCurrentUser().getUid();

        FloatingActionButton edit= (FloatingActionButton) findViewById(R.id.fab_edit);
        FloatingActionButton world= (FloatingActionButton) findViewById(R.id.fab3);

        world.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(MainActivity.this, mAuth.getCurrentUser().getUid(),
                      //  Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, WorldActivity.class);
                startActivity(i);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(i);
            }
        });


        FloatingActionButton logOut= (FloatingActionButton) findViewById(R.id.main_logout);

        mFirestoreRead= FirebaseFirestore.getInstance();
        mFirestoreEx= FirebaseFirestore.getInstance();

       /* mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
              currentUserName= (String) documentSnapshot.get("name");
            }
        });*/


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                Intent i= new Intent(MainActivity.this,WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        });


        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }*/

                Log.d("FirstU",email);
                Toast.makeText(MainActivity.this, uid2,
                        Toast.LENGTH_SHORT).show();

            }
        });




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        enrollListener= new KairosListener() {
    @Override
    public void onSuccess(String response) {
        Log.d("KAIROS DEMO", "Done:"+response);
        Toast.makeText(MainActivity.this, "Done",
                Toast.LENGTH_SHORT).show();
        saveUserData();

    }

    @Override
    public void onFail(String s) {

    }
};

delete= new KairosListener() {
    @Override
    public void onSuccess(String s) {
        Log.d("delete",s);
    }

    @Override
    public void onFail(String s) {

    }
};
        listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                Log.d("KAIROS DEMO", response);
                if (response.contains("no match found")){
                    uuid = UUID.randomUUID().toString().replace("-", "");
                    mProgressDialog.show();
                    createAccount(uuid+GMAIL, PASS);
                   /* try {
                        myKairos.enroll(photo, "Khalil", galleryId, null, null, null, enrollListener);;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }*/
                    Log.d("Here","true");

                }else if (response.contains("Error")){

                    Toast.makeText(MainActivity.this, "No face was detected. Try again. ",
                            Toast.LENGTH_SHORT).show();

                }else{


                    Log.d("blabla","success");
                    JSON json = new JSON(response);
                    String id=json.key("images").index(0).key("candidates").index(0).key("subject_id").stringValue();
                    Log.d("kd", id);
                    test=id;
                    String[] parts = id.split("_");
                    final String idPart1 = parts[0];


                    Log.d("info2","Heree");
                  // try {
                        Intent i= new Intent(MainActivity.this,RatingActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("userID",idPart1);
                        i.putExtras(bundle);
                        startActivity(i);

                       /* DocumentReference docRef = mFirestore.collection("Users").document(idPart1);

                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                               // String name= (String) documentSnapshot.get("name");
                                // String imageUri= (String) documentSnapshot.get("imageUri");
                                Intent i= new Intent(MainActivity.this,RatingActivity.class);
                               // Bundle bundle= new Bundle();
                                //bundle.putString("currentUserName",currentUserName);
                                //i.putExtras(bundle);
                                startActivity(i);

                            }
                        });*/

                       /* mFirestore.collection("Users").document(idPart1).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                               // double rate = (double) documentSnapshot.get("rating");

                                String name= (String) documentSnapshot.get("name");
                                String imageUri= (String) documentSnapshot.get("imageUri");
                                Log.d("info",name+ ", "+imageUri);
                               // Uri myUri = Uri.parse(imageUri);

                                Intent i= new Intent(MainActivity.this,RatingActivity.class);


                                Bundle bundle= new Bundle();


                               // bundle.putString("name",name);
                              // bundle.putString("imageUri",imageUri);



                                bundle.putString("currentUserName",currentUserName);
                                i.putExtras(bundle);

                                startActivity(i);




                            }
                        });*/

                   /* }catch (NullPointerException e){
                        Log.d("Error",e.toString());
                    }*/


                }
                /*JSON json = new JSON(response);
                Log.d("kd", json.key("images").index(0).key("candidates").index(0).key("subject_id").stringValue());

                JSONObject obj= json.getJsonObject();
                try {
                    JSONArray moviesArray = obj.getJSONArray("images");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d("kd", obj.getJSONArray("images").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onFail(String response) {
                Log.d("KAIROS DEMO", "Here: "+response);
            }
        };



        myKairos = new Kairos();

        /* * * set authentication * * */
        String app_id = "abdf65f3";
        String api_key = "8aedb3480325fd88402b184b795f44ff";
        myKairos.setAuthentication(this, app_id, api_key);

      //  try {


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

            /* * * * * * * * * * * * * * * * * * * * */
            /* * *  Kairos Method Call Examples * * */
            /* * * * * * * * * * * * * * * * * * * */
            /* * * * * * * * * * * * * * * * * * **/
            /* * * * * * * * * * * * * * * * * * */
            /* * * * * * * * * * * * * * * * * **/
            /* * * * * * * * * * * * * * * * * */
            /* * * * * * * * * * * * * * * * **/
            /* * * * * * * * * * * * * * * * */


            //  List galleries

            // String image = "http://media.kairos.com/liz.jpg";
            // String subjectId = "Elizabeth";
            // String galleryId = "users";
            // myKairos.enroll(image, subjectId, galleryId, null, null, null, listener);
           // myKairos.listGalleries(listener);
            /* * * * * * * * DETECT EXAMPLES * * * * * * *


            // Bare-essentials Example:
            // This example uses only an image url, setting optional params to null
            String image = "http://media.kairos.com/liz.jpg";
            myKairos.detect(image, null, null, listener);



            // Fine-grained Example:
            // This example uses a bitmap image and also optional parameters
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.liz);
            String selector = "FULL";
            String minHeadScale = "0.25";
            myKairos.detect(image, selector, minHeadScale, listener);





            /* * * * * * * * ENROLL EXAMPLES * * * * * * *

            // Bare-essentials Example:
            // This example uses only an image url, setting optional params to null



            // Fine-grained Example:
            // This example uses a bitmap image and also optional parameters
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.liz);
            String subjectId = "Elizabeth";
            String galleryId = "friends";
            String selector = "FULL";
            String multipleFaces = "false";
            String minHeadScale = "0.25";
            myKairos.enroll(image,
                    subjectId,
                    galleryId,
                    selector,
                    multipleFaces,
                    minHeadScale,
                    listener);

                    */


            /* * * * * * * RECOGNIZE EXAMPLES * * * * * * *

            // Bare-essentials Example:
            // This example uses only an image url, setting optional params to null



            // Fine-grained Example:
            // This example uses a bitmap image and also optional parameters
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.liz);
            String galleryId = "friends";
            String selector = "FULL";
            String threshold = "0.75";
            String minHeadScale = "0.25";
            String maxNumResults = "25";
            myKairos.recognize(image,
                    galleryId,
                    selector,
                    threshold,
                    minHeadScale,
                    maxNumResults,
                    listener);

                    */


            /* * * * GALLERY-MANAGEMENT EXAMPLES * * * *


            //  List galleries
            myKairos.listGalleries(listener);



            //  List subjects in gallery
            myKairos.listSubjectsForGallery("your_gallery_name", listener);



            // Delete subject from gallery
            myKairos.deleteSubject("your_subject_id", "your_gallery_name", listener);



            // Delete an entire gallery
            myKairos.deleteGallery("your_gallery_name", listener);

            */



      /*  } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
             photo = (Bitmap) data.getExtras().get("data");
             Log.d("HEIGHT",photo.getHeight()+"");
             //test= (Uri) data.getExtras().get("data");
           // imageView.setImageBitmap(photo);
            try {
                Log.d("ICE","ICEN");
                myKairos.recognize(photo, galleryId, null, null, null, null, listener);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void createAccount(String e, String password) {

        mAuth2.createUserWithEmailAndPassword(e, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth2.getCurrentUser();
                            otherUserID= user.getUid();
                            Log.d("uid",otherUserID);


                            try {
                                myKairos.enroll(photo, otherUserID+"_"+uuid, galleryId, null, null, null, enrollListener);
                                test=otherUserID+"_"+uuid;



                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }




                         /*   try {
                                myKairos.enroll(photo, userID+"_"+uuid, galleryId, null, null, null, enrollListener);;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }*/


                            //updateUI(user);
                        }else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                       // mProgressDialog.hide();
                       // mProgressDialog.dismiss();
                        mProgressDialog.hide();
                    }
                });
        // [END create_user_with_email]
    }

    private void saveUserData(){
        User user= new User("","",0.0,null,false,0,0);

        Log.d("otherUID","other: "+otherUserID);
        mFirestoreEx.collection("Users").document(otherUserID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d("ALO","HERERE");


                uploadBitmaptoStorage();
                Log.d("ice", "DocumentSnapshot successfully written!");


            }





        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ALO","ERROR: "+e.toString());
            }
        });





    }

    private void uploadBitmaptoStorage(){
        Log.d("ALO","Hey:"+ photo.getHeight());
        StorageReference filePath= mStorageReference.child("UsersProfilePics").child(otherUserID);
      //  Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = filePath.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("ALO","NOP");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("ALO","YEP");
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mFirestoreRead.collection("Users").document(otherUserID).update("imageUri",downloadUrl.toString());
                mAuth2.signOut();
                mAuth.signInWithEmailAndPassword(email,"wasabi").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent i= new Intent(MainActivity.this,RatingActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("userID",otherUserID);
                        //  bundle.putString("currentUserName",currentUserName);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });

            }
        });
    }


}
