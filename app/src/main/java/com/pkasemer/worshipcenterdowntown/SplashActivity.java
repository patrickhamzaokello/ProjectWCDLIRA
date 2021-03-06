package com.pkasemer.worshipcenterdowntown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 3000;
    SharedPreferences onboarding_sharedPreferences;

    Animation topAnim, bottomAnim;
    TextView s_logo, s_slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_activity);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        actionBar.hide();
        //animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.splashbottom_animation);

        s_logo = findViewById(R.id.s_logo);
        s_slogan = findViewById(R.id.s_slogan);

        s_logo.setAnimation(topAnim);
        s_slogan.setAnimation(bottomAnim);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onboarding_sharedPreferences = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onboarding_sharedPreferences.getBoolean("firstTime", true);

                if(isFirstTime){

                    SharedPreferences.Editor editor = onboarding_sharedPreferences.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent intent = new Intent(SplashActivity.this, OnBoarding.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, WelcomeChurch.class);
                    startActivity(intent);


                }
                finish();


            }
        }, SPLASH_SCREEN);

    }

}