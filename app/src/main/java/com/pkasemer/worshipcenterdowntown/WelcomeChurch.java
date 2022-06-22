package com.pkasemer.worshipcenterdowntown;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkasemer.worshipcenterdowntown.HelperClasses.SharedPrefManager;

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

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, RootActivity.class));
            return;
        }


        //animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.splashbottom_animation);


        introtext = findViewById(R.id.introtext);
        slogan = findViewById(R.id.secondintrotext);
        thirdIntro = findViewById(R.id.thirdIntro);

        introtext.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);
        thirdIntro.setAnimation(bottomAnim);


        findViewById(R.id.w_create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeChurch.this, RegisterMaterial.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.w_login_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeChurch.this, LoginMaterial.class);
                startActivity(intent);
                finish();
            }
        });

    }
}