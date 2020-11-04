package com.skywalkers.hotelmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    EditText username, password;
    TextView SignUpBtn, captionText;
    ImageView fadingView;
    Button btnSubmit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mAuth=FirebaseAuth.getInstance();

        //fading imageView values
        fadingView = findViewById(R.id.fadingView);
        captionText = findViewById(R.id.captionText);
        //image array
        int[] imagesToShow = {R.drawable.hotel_1, R.drawable.hotel_2, R.drawable.hotel_3, R.drawable.hotel_4, R.drawable.hotel_5};
        String[] captions = {"Welcome to Translyvania Services", "We provide Quick Services", "Comfy Beds", "Delicious Food", "Lavish Amenities"};
        //utilizing the function for fade animations
        animate(fadingView, captionText, imagesToShow, captions, 0, false);

        //main activity functions
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        SignUpBtn = findViewById(R.id.SignUpBtn);
        btnSubmit = findViewById(R.id.btnSubmit);





        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this,
                        com.skywalkers.hotelmanagement.SignUp.class);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrnm = username.getText().toString().trim();
                String pswd = password.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(usrnm,pswd)
                        .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user=mAuth.getCurrentUser();
                                    Intent intent = new Intent(LoginPage.this,
                                            com.skywalkers.hotelmanagement.ButtonPage.class);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(LoginPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }



    //reference https://stackoverflow.com/questions/8720626/android-fade-in-and-fade-out-with-imageview //

    //custom function for fading animations
    public void animate(final ImageView imageView, final TextView textView, final int images[], final String captions[], final int index, final boolean flag) {
        //set time values for fades
        int fadeInDuration = 500;
        int timeBetween = 2000;
        int fadeOutDuration = 1000;

        //set default view
        imageView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        imageView.setImageResource(images[index]);
        textView.setText(captions[index]);
        //for fading in
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(fadeInDuration);
        //for fade out
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);
        //inserting these animation values
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        imageView.setAnimation(animation);
        textView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //bullshit function
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (images.length - 1 > index)
                    animate(imageView, textView, images, captions, index + 1, flag);
                else if (!flag)
                    animate(imageView, textView, images, captions, 0, flag);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //bullshit function
            }
        });
    }

}