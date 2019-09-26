package com.example.queueskip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashLayout=findViewById(R.id.splashlayout);
        splashLayout.setOnClickListener(this);


        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, secondsDelayed * 2000);


    }

    public void onClick(View view) {
        startActivity(new Intent(this,LoginActivity.class)); //Maybe change to Login
        finish();
    }//end of onClick
}

