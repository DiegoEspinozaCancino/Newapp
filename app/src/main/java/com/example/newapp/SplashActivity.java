package com.example.newapp;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar Progressbar;
    int progress = 0;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Progressbar = (ProgressBar) findViewById(R.id.progressBar);

    //================= RUNNABLE PROGRESS BAR + THREAD ============================
        new Thread(new Runnable() {
            public void run() {
                while (progress < 100) {
                    progress += 5;

                    myHandler.post(new Runnable() {
                        public void run() {
                            Progressbar.setProgress(progress);
                        }

                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        }).start();

    }


}