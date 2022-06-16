package com.pkasemer.worshipcenterdowntown;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeChurch extends AppCompatActivity {

    SharedPreferences onboarding_sharedPreferences;

    Animation topAnim, bottomAnim;
    TextView introtext, slogan,thirdIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_church);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        actionBar.hide();
        //animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.splashbottom_animation);


        introtext = findViewById(R.id.introtext);
        slogan = findViewById(R.id.secondintrotext);
        thirdIntro = findViewById(R.id.thirdIntro);

        introtext.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);
        thirdIntro.setAnimation(bottomAnim);


    }
}