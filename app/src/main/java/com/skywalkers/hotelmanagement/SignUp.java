package com.skywalkers.hotelmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText etName,etEmail,etPassword,etNumber,etAddress;
    Button btnSubmit;
    //ImageView ivPicture;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageRef;
    Uri filePath;
    final int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth=FirebaseAuth.getInstance();

        etName=findViewById(R.id.etName);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etNumber=findViewById(R.id.etNumber);
        etAddress=findViewById(R.id.etAddress);
        btnSubmit=findViewById(R.id.btnSubmit);
        //ivPicture=findViewById(R.id.ivPicture);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        storage=FirebaseStorage.getInstance();
        storageRef=storage.getReference();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    onAuthSuccess(user);
                                    //UploadImage();
                                    Intent intent = new Intent(SignUp.this,
                                            com.skywalkers.hotelmanagement.LoginPage.class);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            
                        });
            }

        });

        /*ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose Image from here..."), PICK_IMAGE_REQUEST);

            }
        });*/

    }

    /*private void UploadImage()
    {
        if (filePath != null) {

            // Defining the child of storageReference
            StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    // Image uploaded successfully
                    Toast.makeText(SignUp.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {

                    // Error, Image not uploaded
                    Toast.makeText(SignUp.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivPicture.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }*/

    private void onAuthSuccess(FirebaseUser user) {
        String name=etName.getText().toString().trim();
        long number=Long.parseLong(etNumber.getText().toString().trim());
        String address=etAddress.getText().toString().trim();
        writeNewUser(user.getUid(),name,user.getEmail(),number,address);

    }

    private void writeNewUser(String userId,String name,String email, long number,String address) {
        myRef.child("Users").child(userId).child("name").setValue(name);
        myRef.child("Users").child(userId).child("email").setValue(email);
        myRef.child("Users").child(userId).child("number").setValue(number);
        myRef.child("Users").child(userId).child("address").setValue(address);
    }

}