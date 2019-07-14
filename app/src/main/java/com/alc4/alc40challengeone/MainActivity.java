package com.alc4.alc40challengeone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        flyIn();
    }

    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_alc_connect:
                Intent alcIntent = new Intent(this, ALC_Connect.class);
                startActivity(alcIntent);
                break;
            case R.id.btn_profile:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                startActivity(profileIntent);
                break;
        }
    }

    private void flyIn() {
        Animation txtAnimation = AnimationUtils.loadAnimation(this, R.anim.app_name_animation);
        Animation logotAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        Animation btn_profileAnimation = AnimationUtils.loadAnimation(this, R.anim.profile_animation);
        Animation btnAbout = AnimationUtils.loadAnimation(this, R.anim.abt_animation);


        findViewById(R.id.logo).startAnimation(logotAnimation);
        findViewById(R.id.welcome).startAnimation(txtAnimation);
        findViewById(R.id.btn_profile).startAnimation(btn_profileAnimation);
        findViewById(R.id.btn_alc_connect).startAnimation(btnAbout);
    }


    long lastPress;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPress > 5000) {
            Toasty.info(this, "Press back again to exit !", Toast.LENGTH_SHORT, true).show();
            lastPress = currentTime;
        } else {
            super.onBackPressed();
        }
    }
}


