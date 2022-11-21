package com.example.newapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newapp.SQLLite.Agregar;

public class MainActivity extends AppCompatActivity {
    private CheckBox chkSeleccionar;
    private EditText editTextTextEmailAddress;              //  LOGIN
    private EditText PassweditTextTextPasswordord;          // CREDENTIALS
    private Button btn_enter;                               //

    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logreg_activity);
        chkSeleccionar = (CheckBox) findViewById(R.id.chkBox); //CHECKBOX DATA

/**        editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        PassweditTextTextPasswordord = (EditText) findViewById(R.id.PassweditTextTextPasswordord);
        btn_enter = (Button) findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextTextEmailAddress.getText().toString().isEmpty() && !PassweditTextTextPasswordord.getText().toString().isEmpty()){
                    Intent intent = new Intent (MainActivity.this, NavDrawer.class );
                    intent.putExtra("nombre", editTextTextEmailAddress.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Debes indicar un nombre", Toast.LENGTH_SHORT).show();
                }
            }
        }));**/


    }

                                    //================== MAIN FORM VIEW ============================

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
                                        // LOGIN BUTTON
    public void Logbtn (View logv){
        Intent inte = new Intent(this, NavDrawer.class);
        System.out.println("llamado de login");
        startActivity(inte);
    }
                                        // REGISTER BUTTON
    public void regView (View formv){
        Intent inte2 = new Intent(this, RegisterActivity.class);
        System.out.println("llamado de registro");
        startActivity(inte2);
    }
                                        // SECOND REGISTER BUTTON [BETA]
    public void regbetabtn (View regdormv){
        Intent RegIntebeta = new Intent(this, Agregar.class);
        startActivity(RegIntebeta);
    }

}