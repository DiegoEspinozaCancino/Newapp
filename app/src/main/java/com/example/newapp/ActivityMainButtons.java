package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newapp.SQLLite.Agregar;

public class ActivityMainButtons extends AppCompatActivity {
    private Button btnmain1, btnmain2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buttons);
    btnmain1 = (Button) findViewById(R.id.btnmain1);
    btnmain2 = (Button) findViewById(R.id.btnmain2);


    btnmain1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent usuarioI = new Intent(ActivityMainButtons.this, MainActivity.class);
            startActivity(usuarioI);
        }
    });
    btnmain2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent usuarioI2 = new Intent(ActivityMainButtons.this, Agregar.class);
            startActivity(usuarioI2);
        }
    });

    }
}
