package com.example.newapp.ui;

import android.app.Activity;
import android.os.Bundle;

import com.example.newapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignOutActivity extends Activity {

    FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        mAuth = FirebaseAuth.getInstance();

        //FirebaseAuth.getInstance().signOut();
    }



}
