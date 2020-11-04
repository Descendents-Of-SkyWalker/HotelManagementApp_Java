package com.skywalkers.hotelmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePage extends AppCompatActivity {

    CircleImageView ivPicture;
    ImageView profile,home,tasks;
    EditText etName,etEmail,etNumber,etAddress;
    Button btnSubmit;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    ArrayList<String> arrList;
    private static final String TAG = "ProfilePage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        arrList= new ArrayList<>();
        ivPicture=findViewById(R.id.ivPicture);
        etName=findViewById(R.id.etName);
        etEmail=findViewById(R.id.etEmail);
        etNumber=findViewById(R.id.etNumber);
        etAddress=findViewById(R.id.etAddress);
        profile=findViewById(R.id.profile);
        home=findViewById(R.id.home);
        tasks=findViewById(R.id.tasks);

        mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference().child("Users").child(user.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrList.add(dataSnapshot.child("name").getValue(String.class));
                arrList.add(dataSnapshot.child("email").getValue(String.class));
                arrList.add(dataSnapshot.child("number").getValue(Long.class).toString());
                arrList.add(dataSnapshot.child("address").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                etName.setText(arrList.get(0));
                etEmail.setText(arrList.get(1));
                etNumber.setText(arrList.get(2));
                etAddress.setText(arrList.get(3));
                etName.setEnabled(false);
                etEmail.setEnabled(false);
                etNumber.setEnabled(false);
                etAddress.setEnabled(false);

            }
        }, 1000);



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastMaker(500,"On Profile");

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfilePage.this,com.skywalkers.hotelmanagement.ButtonPage.class);
                startActivity(intent);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfilePage.this,com.skywalkers.hotelmanagement.TasksActivity.class);
                startActivity(intent);
            }
        });
    }
    public void ToastMaker(int time,String text){
        final Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, time);
    }
}