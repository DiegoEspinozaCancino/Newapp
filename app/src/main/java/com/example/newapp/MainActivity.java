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
    private EditText email, password;              //   LOGIN
    private Button loginbutton;                   //    CREDENTIALS
    FirebaseAuth mAuth;


    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logreg_activity);
        chkSeleccionar = (CheckBox) findViewById(R.id.chkBox); //CHECKBOX DATA


        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        loginbutton =(Button) findViewById(R.id.btn_enter);
        mAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));

                                                            //LECTURA DE BUTTON LOGIN
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String emailUser = email.getText().toString().trim();
                String passUser = password.getText().toString().trim();

                if(emailUser.isEmpty() && passUser.isEmpty()){
                    Toast.makeText(MainActivity.this, "Debes llenar los campos para ingresar", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginUser(emailUser,passUser);
                }
            }
        });

    }
                                                            //LOG DE CARGA DE DATOS DESDE LA DATA BASE
    private void loginUser(String emailUser, String passUser) {
        mAuth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if (task.isSuccessful()) {

                    finish();
                    startActivity(new Intent(MainActivity.this, NavDrawer.class));
                }
                else{
                    Toast.makeText(MainActivity.this, "WHOPS!... Hubo un error, intentelo nuevamente", Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error al iniciar sesión", Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(MainActivity.this, NavDrawer.class));
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
                                        // SECOND REGISTER BUTTON [BETA]
    public void regbetabtn (View regdormv){
        Intent RegIntebeta = new Intent(this, Agregar.class);
        startActivity(RegIntebeta);
    }

}