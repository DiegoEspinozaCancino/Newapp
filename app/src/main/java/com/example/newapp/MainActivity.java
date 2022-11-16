package com.example.newapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CheckBox chkSeleccionar;
    private EditText Email;
    private EditText Password;

    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logreg_layout);
        chkSeleccionar = (CheckBox) findViewById(R.id.chkBox);

    }


    public void logChk (View v){
        if (chkSeleccionar.isChecked() == true) {
            String mensaje = "Mantener sesion: activada";
            Toast toast = Toast.makeText(this, "Mantener sesion: activada", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.START, 90, 0);
            toast.show();
        } else {

            String mensaje = "Mantener sesion: desactivada";
            Toast.makeText(this, mensaje,
                    Toast.LENGTH_LONG).show();
        }
    }
    //================== LLAMADOS DE ACTIVITIES ============================
    /**public void Logbtn (View logv){
        Intent inte = new Intent(this, MainRecyclerView.class);
        System.out.println("llamado de login");
        startActivity(inte);
    }**/
    public void Logbtn (View logv){
        Intent inte = new Intent(this, NavDrawer.class);
        System.out.println("llamado de login");
        startActivity(inte);
    }
    public void regView (View formv){
        Intent inte2 = new Intent(this, RegisterActivity.class);
        System.out.println("llamado de registro");
        startActivity(inte2);
    }
}