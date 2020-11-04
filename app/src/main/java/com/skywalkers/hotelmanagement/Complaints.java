package com.skywalkers.hotelmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Complaints extends AppCompatActivity {

    String[] Examples={"Electronics Not Working","Room Property Damaged","Mechanical Things not Working","Food Quality No Adequate","Other Reasons"};
    final int[] images={R.drawable.broken_wire,R.drawable.broken_window,R.drawable.mantainence,R.drawable.no_food,R.drawable.no_means_no};
    DatabaseReference myRef;
    Spinner spinner;
    private FirebaseAuth mAuth;
    EditText complaints;
    Button cancel_button,submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        spinner=(Spinner)findViewById(R.id.custom_spinner);
        submit_button=findViewById(R.id.submit_button);
        cancel_button=findViewById(R.id.cancel_button);
        complaints=findViewById(R.id.complaints);

        spinner.setAdapter(new DropDownAdapter(Complaints.this,R.layout.custom_spinner,Examples,Examples,images));

        mAuth=FirebaseAuth.getInstance();
        myRef=FirebaseDatabase.getInstance().getReference();

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complaintType=spinner.getSelectedItem().toString();
                String complaintDescription=complaints.getText().toString().trim();
                String UserID=mAuth.getUid();
                myRef.child("Users").child(UserID).child("Tasks").child("Complaints").child("complaintType").setValue(complaintType);
                myRef.child("Users").child(UserID).child("Tasks").child("Complaints").child("complaintDescription").setValue(complaintDescription);
                ToastMaker(100,"Complaint Recorded");
                startActivity(new Intent(Complaints.this,
                        com.skywalkers.hotelmanagement.ButtonPage.class));
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastMaker(100,"Cancelled");
                startActivity(new Intent(Complaints.this,
                        com.skywalkers.hotelmanagement.ButtonPage.class));
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