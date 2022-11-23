package com.example.newapp;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.newapp.SQLLite.Agregar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private CheckBox chkSeleccionar;
    private EditText e_mail, passw;              //   LOGIN
    //private Button loginbutton, btnregister;    //    CREDENTIALS
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logreg_activity);
        chkSeleccionar = (CheckBox) findViewById(R.id.chkBox); //CHECKBOX DATA


        e_mail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        passw = (EditText) findViewById(R.id.editTextTextPassword);
        /**loginbutton = (Button) findViewById(R.id.btn_enter);
        btnregister = (Button) findViewById(R.id.btn_reg);**/

        mAuth = FirebaseAuth.getInstance();
    }
    public void Logbtn(View view){

        try {
            mAuth.signInWithEmailAndPassword(e_mail.getText().toString().trim(), passw.getText().toString())
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent logInte = new Intent(MainActivity.this, NavDrawer.class);
                                startActivity(logInte);
                            } else {
                                Toast.makeText(MainActivity.this, "WHOPS!... Hubo un error, verifique su correo y contraseña estén bien ingresadas", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        catch(Exception e){
            Toast.makeText(MainActivity.this, "Ha ocurrido un error, intentelo nuevamente", Toast.LENGTH_SHORT).show();
        }
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

                                        // REGISTER BUTTON
    public void regView (View formv){
        Intent inte2 = new Intent(this, RegisterActivity.class);
        System.out.println("llamado de registro");
        startActivity(inte2);
    }
    /**                                    // SECOND REGISTER BUTTON [BETA]
    public void regbetabtn (View regdormv){
        Intent RegIntebeta = new Intent(this, Agregar.class);
        startActivity(RegIntebeta);
    }**/

}