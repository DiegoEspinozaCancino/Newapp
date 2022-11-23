package com.example.newapp.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.newapp.ActivityMainButtons;
import com.example.newapp.MainActivity;
import com.example.newapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserFragment extends AppCompatActivity {

    private Button btn_exit;
    FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);

        btn_exit = (Button) findViewById(R.id.btn_exit);
        mAuth = FirebaseAuth.getInstance();

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(UserFragment.this, ActivityMainButtons.class));
            }
        });
    }


}
