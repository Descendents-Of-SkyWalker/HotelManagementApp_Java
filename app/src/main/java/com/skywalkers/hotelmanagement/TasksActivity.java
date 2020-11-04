package com.skywalkers.hotelmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TasksActivity extends AppCompatActivity {
    TextView serviceTaskID,tvLaundry,tvRoomService;
    ImageView profile,home,tasks;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private static final String TAG = "TasksActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        serviceTaskID=findViewById(R.id.serviceTaskID);
        tvLaundry=findViewById(R.id.tvLaundry);
        tvRoomService=findViewById(R.id.tvRoomService);
        profile=findViewById(R.id.profile);
        home=findViewById(R.id.home);
        tasks=findViewById(R.id.tasks);

        database = FirebaseDatabase.getInstance();
        mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        myRef=database.getReference().child("Users").child(user.getUid()).child("Tasks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String task="No. of Clothes:\n"+snapshot.child("Laundry").child("clothes").getValue(String.class)+"\nPick up time:\n"+snapshot.child("Laundry").child("time").getValue(String.class);
                tvLaundry.setText(task);
                task="Preferred time:\n"+snapshot.child("RoomService").child("time").getValue(String.class);
                tvRoomService.setText(task);
                task="";
                for(DataSnapshot ds:snapshot.child("Food").getChildren())
                {
                    task=task+ds.getKey()+" "+ds.getValue().toString()+"\n";
                }
                serviceTaskID.setText(task);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TasksActivity.this,com.skywalkers.hotelmanagement.ProfilePage.class);
                startActivity(intent);

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TasksActivity.this,com.skywalkers.hotelmanagement.ButtonPage.class);
                startActivity(intent);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastMaker(500,"In Tasks");
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