package com.example.newapp.recyclerview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.newapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView recyclerViewUser;
    private  RecyclerAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycler);

        recyclerViewUser = (androidx.recyclerview.widget.RecyclerView)findViewById(R.id.recyclerUsers);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new RecyclerAdapter(gettingUsers());
        recyclerViewUser.setAdapter(userAdapter);
    }


    public List<UserModel> gettingUsers(){
        List <UserModel> User = new ArrayList<>();
        User.add(new UserModel("annon","12-09-2022",R.drawable.vlogo));
        User.add(new UserModel("El_azikhalao20","12-09-2022",R.drawable.kidselfie));
        User.add(new UserModel("Andrea","12-09-2022",R.drawable.girlpic));
        User.add(new UserModel("Sandra","12-09-2022",R.drawable.animepic));
        User.add(new UserModel("annon","12-09-2022",R.drawable.vlogo));
        User.add(new UserModel("Andres","12-09-2022",R.drawable.userlogo2));
        User.add(new UserModel("El_azikhalao20","12-09-2022",R.drawable.kidselfie));
        User.add(new UserModel("Sandra","12-09-2022",R.drawable.animepic));
        User.add(new UserModel("annon","12-09-2022",R.drawable.vlogo));
        User.add(new UserModel("Andres","12-09-2022",R.drawable.userlogo2));
        User.add(new UserModel("Andrea","12-09-2022",R.drawable.girlpic));
        User.add(new UserModel("El_azikhalao20","12-09-2022",R.drawable.kidselfie));
        User.add(new UserModel("annon","12-09-2022",R.drawable.vlogo));
        User.add(new UserModel("Andres","12-09-2022",R.drawable.userlogo2));
        User.add(new UserModel("El_azikhalao20","12-09-2022",R.drawable.kidselfie));
        User.add(new UserModel("Sandra","12-09-2022",R.drawable.animepic));
        return User;
    }

}