package com.skywalkers.hotelmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FoodOrder extends AppCompatActivity implements FoodItemAdapter.itemClicked{

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<FoodMenuItem> menuItems;
    ArrayList<Integer> quantity;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    ImageView ivProfile,ivOkay,ivTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order);

        ivProfile=findViewById(R.id.ivProfile);
        ivOkay=findViewById(R.id.ivOkay);
        ivTasks=findViewById(R.id.ivTasks);

        menuItems=new ArrayList<>();
        quantity=new ArrayList<>();
        recyclerView=findViewById(R.id.foodList);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        menuItems.add(new FoodMenuItem("Fried Chicken","nonveg",150,"Bleh"));
        menuItems.add(new FoodMenuItem("Pasta Arrabiata","veg",175,"Bleh"));
        menuItems.add(new FoodMenuItem("Biriyani","nonveg",230,"Bleh"));
        menuItems.add(new FoodMenuItem("Lasagna","veg",345,"Bleh"));
        menuItems.add(new FoodMenuItem("Chicken Sandwich","nonveg",120,"Bleh"));
        menuItems.add(new FoodMenuItem("Dhokla","veg",90,"Bleh"));
        menuItems.add(new FoodMenuItem("Veg Burger","veg",175,"Bleh"));
        menuItems.add(new FoodMenuItem("Non Veg Burger","nonveg",190,"Bleh"));
        menuItems.add(new FoodMenuItem("Brownie","veg",225,"Bleh"));
        menuItems.add(new FoodMenuItem("Pizza","nonveg",350,"Bleh"));

        for(int i=0;i<menuItems.size();i++)
        {
            quantity.add(0);
        }

        adapter=new FoodItemAdapter(this,menuItems);
        recyclerView.setAdapter(adapter);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FoodOrder.this,com.skywalkers.hotelmanagement.ProfilePage.class);
                startActivity(intent);
            }
        });

        ivOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FoodOrder.this,com.skywalkers.hotelmanagement.ButtonPage.class);
                startActivity(intent);
            }
        });

        ivTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FoodOrder.this,com.skywalkers.hotelmanagement.TasksActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemClicked(int index) {
        String orderItem=menuItems.get(index).getItemName();
        int val=quantity.get(index);
        myRef=database.getReference().child("Users").child(user.getUid()).child("Tasks").child("Food");
        myRef.child(orderItem).setValue(""+(++val));
        quantity.set(index,val);
        ToastMaker(500,"Item Added");
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