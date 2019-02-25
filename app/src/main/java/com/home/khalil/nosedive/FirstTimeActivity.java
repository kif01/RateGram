package com.home.khalil.nosedive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstTimeActivity extends AppCompatActivity {
    EditText nameET;
    EditText bioET;
    TextView counterTV;
    ImageButton submit;
    CircleImageView profileImage;
    private Uri imageUri;
    String userID;
    private FirebaseAuth mAuth;
    int count=0;
    boolean b=false;
    Context mContext;
    private static int RESULT_LOAD_IMG = 1;
    private StorageReference mStorageReference;
    private FirebaseFirestore mFirestore;
    private FirebaseFirestore mFirestoreRead;
    Intent i;

    Bitmap photo;
    String uuid;

    String galleryId = "users";
    String app_id = "abdf65f3";
    String api_key = "8aedb3480325fd88402b184b795f44ff";
    final String PASS= "wasabi";
    final String GMAIL="@gmail.com";

    Kairos myKairos;
    KairosListener listener;
    private KairosListener enrollListener;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_first_time);
        Bundle bundle = getIntent().getExtras();
        photo = bundle.getParcelable("image");
        mStorageReference= FirebaseStorage.getInstance().getReference();
        mContext= this;
        nameET= (EditText) findViewById(R.id.setup_name);
        bioET= (EditText) findViewById(R.id.setup_bio);
        counterTV= (TextView) findViewById(R.id.counter);
         profileImage= (CircleImageView) findViewById(R.id.setup_image) ;
        profileImage.setImageBitmap(photo);
        mAuth=FirebaseAuth.getInstance();

        mProgressDialog= new ProgressDialog(this,R.style.MyAlertDialogStyle);

        mProgressDialog.setMessage("Creating Account...");

        submit = (ImageButton) findViewById(R.id.profile_submit);
        submit.setEnabled(false);
        mFirestore= FirebaseFirestore.getInstance();
        mFirestoreRead= FirebaseFirestore.getInstance();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMG);
            }
        });

        myKairos = new Kairos();
        myKairos.setAuthentication(this, app_id, api_key);


        nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String currentText = editable.toString();
                int currentLength = currentText.length();
                if(currentLength >=6){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        submit.setBackground(ContextCompat.getDrawable(mContext,
                                R.drawable.circular_shape_valid));
                        submit.setEnabled(true);
                    }
                    submit.setImageResource(R.drawable.ic_done_valid);
                    Log.d("length",currentLength+"");
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        submit.setBackground(ContextCompat.getDrawable
                                (mContext, R.drawable.circular_shape));
                        submit.setImageResource(R.drawable.ic_done);
                        submit.setEnabled(false);
                    }
                }
            }
        });


        bioET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            counterTV.setVisibility(View.VISIBLE);

           // count++;
           // counterTV.setText(count+"/250");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String currentText = editable.toString();
                int currentLength = currentText.length();
                counterTV.setText(currentLength+"/250");
                if (currentLength>250){
                    submit.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        submit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular_shape));
                    }
                    submit.setImageResource(R.drawable.ic_done);
                }else if(currentLength<=250 && nameET.getText().toString().length()>=6){
                    submit.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        submit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular_shape_valid));

                    }
                    submit.setImageResource(R.drawable.ic_done_valid);
                }

            }
        });

        enrollListener= new KairosListener() {
            @Override
            public void onSuccess(String s) {
                Log.d("FTA_KAIROS",s);
                storeUserData(nameET.getText().toString(), bioET.getText().toString());

            }

            @Override
            public void onFail(String s) {

            }
        };


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(FirstTimeActivity.this, MainActivity.class));
               // finish();
                uuid= UUID.randomUUID().toString().replace("-", "");
                SaveSharedPreference.setPref(FirstTimeActivity.this,"true");
                createAccount(uuid+GMAIL,PASS);





            }
        });
    }

    private void uploadImagetoStorage(){
        Log.d("here","waw");
        StorageReference filePath= mStorageReference.child("UsersProfilePics").child(userID);

        filePath.putFile(imageUri).addOnSuccessListener
                (new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("test1","Image successfully uploaded");
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mFirestoreRead.collection("Users").document(userID).update
                        ("imageUri",downloadUrl.toString()).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                         i = new Intent(FirstTimeActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("test2","Error");
            }
        });
    }

    private void uploadBitmaptoStorage(){
        StorageReference filePath= mStorageReference.child("UsersProfilePics").child(userID);
        Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = filePath.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mProgressDialog.hide();
                mProgressDialog.dismiss();
                mFirestoreRead.collection("Users").document(userID).update("imageUri",downloadUrl.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        i = new Intent(FirstTimeActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
    }

    private void storeUserData(String name, String bio){
        User user= new User(name,bio,0.0,null,
                true,0,0);
        mFirestore.collection("Users").document(userID).set(user).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (imageUri != null) {

                            uploadImagetoStorage();

                                }else{
                            uploadBitmaptoStorage();
                        }

                        }





                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ice", "DocumentSnapshot successfully written!");
                    }
                });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri imgUri = data.getData();
                profileImage.setImageURI(imgUri);
                imageUri = imgUri;

            }
        } catch (Exception e) {
            Toast.makeText(this, "Oops! Something went wrong.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void createAccount(String email, String password) {
        mProgressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userID= mAuth.getCurrentUser().getUid();
                            try {
                                myKairos.enroll(photo, userID+"_"+uuid, galleryId,
                                        null, null,
                                        null, enrollListener);;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        } else {

                            Toast.makeText(FirstTimeActivity.this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }




}
