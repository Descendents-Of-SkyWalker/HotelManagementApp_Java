package com.skywalkers.hotelmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.util.ArrayList;

public class ButtonPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    ImageButton ibtnTaxi,ibtnFood,ibtnLaundry,ibtnRoom;
    Button complaints;
    ImageView profile,home,tasks;
    TextView tvWelcome;
    int val=0;
    int hour=0;
    int min=0;
    String period="AM";
    String name;
    private static final String TAG = "ButtonPage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_page);
        ibtnTaxi=findViewById(R.id.ibtnTaxi);
        ibtnFood=findViewById(R.id.ibtnFood);
        ibtnLaundry=findViewById(R.id.ibtnLaundry);
        ibtnRoom=findViewById(R.id.ibtnRoom);
        complaints=findViewById(R.id.complaints);
        profile=findViewById(R.id.profile);
        home=findViewById(R.id.home);
        tasks=findViewById(R.id.tasks);
        tvWelcome=findViewById(R.id.tvWelcome);

        mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        tvWelcome.setText("Welcome");

        myRef = database.getReference().child("Users").child(user.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name=dataSnapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvWelcome.setText("Welcome "+ name);
            }
        }, 3000);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ButtonPage.this,com.skywalkers.hotelmanagement.ProfilePage.class);
                startActivity(intent);
            }
        });



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastMaker(500,"In Home");
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ButtonPage.this,com.skywalkers.hotelmanagement.TasksActivity.class);
                startActivity(intent);
            }
        });

        ibtnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ButtonPage.this,com.skywalkers.hotelmanagement.FoodOrder.class);
                startActivity(intent);
            }
        });

        ibtnTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.olacabs.customer");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
                else
                {
                    Uri uri = Uri.parse("market://details?id=com.olacabs.customer");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.olacabs.customer")));
                    }
                }
            }
        });

        ibtnRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoomCleaningAlertDialogue(view);
            }
        });

        ibtnLaundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaundryAlertDialogue(view);
            }
        });

        complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ButtonPage.this,
                        com.skywalkers.hotelmanagement.Complaints.class);
                startActivity(intent);
            }
        });
    }

    public void RoomCleaningAlertDialogue(final View v)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Set Time for Room Cleaning");
        final View layout=getLayoutInflater().inflate(R.layout.alert_dialogue_4,null);
        final TextView tvTimeDisplay;
        final ImageView ivTime;
        ivTime=layout.findViewById(R.id.ivTime);
        tvTimeDisplay=layout.findViewById(R.id.tvTimeDisplay);
        ivTime.setVisibility(View.VISIBLE);
        tvTimeDisplay.setVisibility(View.GONE);
        builder.setView(layout);
        builder.setCancelable(true);

        ivTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeAlertDialogue(view,tvTimeDisplay,ivTime);

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                myRef=database.getReference().child("Users").child(user.getUid()).child("Tasks").child("RoomService");
                myRef.child("time").setValue(String.format("%02d",hour)+" : "+String.format("%02d",min)+" "+period);
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public void LaundryAlertDialogue(final View v)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Set number of Clothes and Pick Up Time");
        final View layout=getLayoutInflater().inflate(R.layout.alert_dialogue_2,null);
        final Button btnUp,btnDown;
        final TextView tvCount,tvTimeDisplay;
        final ImageView ivTime;
        btnUp=layout.findViewById(R.id.btnUp);
        btnDown=layout.findViewById(R.id.btnDown);
        tvCount=layout.findViewById(R.id.tvCount);
        ivTime=layout.findViewById(R.id.ivTime);
        tvTimeDisplay=layout.findViewById(R.id.tvTimeDisplay);
        ivTime.setVisibility(View.VISIBLE);
        tvTimeDisplay.setVisibility(View.GONE);
        builder.setView(layout);
        builder.setCancelable(true);


        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val++;
                tvCount.setText(""+val);
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(val>0)
                {
                    val--;
                    tvCount.setText(""+val);
                }
                else
                {
                    ToastMaker(500,"Enter Value Higher than 0");
                }
            }
        });
        ivTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeAlertDialogue(view,tvTimeDisplay,ivTime);

            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                myRef=database.getReference().child("Users").child(user.getUid()).child("Tasks").child("Laundry");
                myRef.child("clothes").setValue(""+val);
                myRef.child("time").setValue(String.format("%02d",hour)+" : "+String.format("%02d",min)+" "+period);
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void SetTimeAlertDialogue(View v, final TextView tvTimeDisplay, final ImageView ivTime)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Set Pick Up Time");
        final View layout=getLayoutInflater().inflate(R.layout.alert_dialogue_3,null);
        final Button btnUp1,btnDown1,btnUp2,btnDown2,btnUp3,btnDown3;
        final TextView tvCount1,tvCount2,tvCount3;
        btnUp1=layout.findViewById(R.id.btnUp1);
        btnUp2=layout.findViewById(R.id.btnUp2);
        btnUp3=layout.findViewById(R.id.btnUp3);
        btnDown1=layout.findViewById(R.id.btnDown1);
        btnDown2=layout.findViewById(R.id.btnDown2);
        btnDown3=layout.findViewById(R.id.btnDown3);
        tvCount1=layout.findViewById(R.id.tvCount1);
        tvCount2=layout.findViewById(R.id.tvCount2);
        tvCount3=layout.findViewById(R.id.tvCount3);
        builder.setView(layout);
        builder.setCancelable(true);
        hour=0;
        min=0;
        period="AM";
        btnUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hour<12)
                {
                    hour++;
                    tvCount1.setText(""+hour);
                }
                else
                {
                    hour=0;
                    tvCount1.setText(""+hour);
                }
            }
        });
        btnDown1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hour>0)
                {
                    hour--;
                    tvCount1.setText(""+hour);
                }
                else
                {
                    hour=12;
                    tvCount1.setText(""+hour);
                }
            }
        });
        btnUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(min<59)
                {
                    min++;
                    tvCount2.setText(""+min);
                }
                else
                {
                    min=0;
                    tvCount2.setText(""+min);
                }

            }
        });
        btnDown2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(min>0)
                {
                    min--;
                    tvCount2.setText(""+min);
                }
                else
                {
                    min=59;
                    tvCount2.setText(""+min);
                }

            }
        });
        btnUp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(period.equals("AM"))
                {
                    period="PM";
                    tvCount3.setText("PM");
                }
                else
                {
                    period="AM";
                    tvCount3.setText("AM");
                }


            }
        });
        btnDown3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(period.equals("AM"))
                {
                    period="PM";
                    tvCount3.setText("PM");
                }
                else
                {
                    period="AM";
                    tvCount3.setText("AM");
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvTimeDisplay.setText(String.format("%02d",hour)+" : "+String.format("%02d",min)+" "+period);
                tvTimeDisplay.setVisibility(View.VISIBLE);
                ivTime.setVisibility(View.GONE);
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
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