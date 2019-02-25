package com.home.khalil.nosedive;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;
    private FirebaseFirestore mFirestore;
    private FirebaseFirestore mFirestoreRead;

    EditText nameET;
    EditText bioET;
    TextView counterTV;
    CircleImageView profileImage;
    String uid;
    private Uri imageUri;
    ImageButton submit;
    Intent i;


    private static int RESULT_LOAD_IMG = 1;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        mFirestore= FirebaseFirestore.getInstance();
        mFirestoreRead= FirebaseFirestore.getInstance();
        mStorageReference= FirebaseStorage.getInstance().getReference();
        mContext=this;



         profileImage= (CircleImageView) findViewById(R.id.edit_profile_image);
         nameET= (EditText) findViewById(R.id.edit_name);
         bioET= (EditText) findViewById(R.id.edit_bio);
         counterTV= (TextView) findViewById(R.id.edit_counter);

        mFirestore.collection("Users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                String uri= (String) documentSnapshot.get("imageUri");
                String name= (String) documentSnapshot.get("name");
                String bio = (String) documentSnapshot.get("bio");
                Picasso.get().load(uri).placeholder(R.drawable.default_profile_pic).into(profileImage);
                nameET.setText(name);
                bioET.setText(bio);

            }
        });

        submit= (ImageButton) findViewById(R.id.edit_profile_submit);
        submit.setEnabled(true);

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
                        submit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular_shape_valid));
                        submit.setEnabled(true);
                    }
                    submit.setImageResource(R.drawable.ic_done_valid);
                    Log.d("length",currentLength+"");
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        submit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular_shape));
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


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMG);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= nameET.getText().toString();
                String bio= bioET.getText().toString();
                storeUserData(name, bio);

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

    private void uploadImagetoStorage(){
        Log.d("here","waw");
        StorageReference filePath= mStorageReference.child("UsersProfilePics").child(uid);

        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("test1","Image successfully uploaded");
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mFirestoreRead.collection("Users").document(uid).update("imageUri",downloadUrl.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // i = new Intent(EditProfileActivity.this,MainActivity.class);
                       // startActivity(i);
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

    private void storeUserData(String name, String bio){
        mFirestore.collection("Users").document(uid).update("name",name,"bio",bio,"isProfilefilled",true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (imageUri!=null){
                    uploadImagetoStorage();
                }else{
                    finish();
                }

            }
        });





    }


}
