package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.newapp.ui.Home_Fragment;
import com.example.newapp.ui.account.UserFragment;
import com.example.newapp.ui.credits.CreditsFragment;
import com.example.newapp.ui.music.MusicPlayerActivity;
import com.google.android.material.navigation.NavigationView;

public class NavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

   private DrawerLayout drawerLayout;
   private NavigationView navigationView;
   private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Home_Fragment());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new Home_Fragment()).commit();
            navigationView.setCheckedItem(R.id.item1);
        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:                                                        //HOME
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new Home_Fragment()).commit();
                break;
            case R.id.item2:                                                        //USER ACCOUNT
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new UserFragment()).commit();
                break;
            case R.id.item3:                                                        //MUSIC PLAYER
                Intent MPIntent = new Intent(this, MusicPlayerActivity.class);
                startActivity(MPIntent);
                break;
            case R.id.item4:                                                        //SETTINGS
                Toast.makeText(NavDrawer.this, "Ajustes", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item5:                                                        //CLASS-ACTIVITIES
                Toast.makeText(NavDrawer.this, "Actividades", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item6:                                                        //CREDITS
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new CreditsFragment()).commit();
                break;
        }
        drawerLayout.closeDrawer((GravityCompat.START));
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer((GravityCompat.START));
        }
        else{
            super.onBackPressed();
        }
    }

}
