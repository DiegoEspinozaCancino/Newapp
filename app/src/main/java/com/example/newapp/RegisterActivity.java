package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //String [] genre = {"Seleccione género", "Masculino", "Femenino", "No binario", "Otros"};
    private EditText email, password, passconfirm;
    //private Button btn_registro;
    FirebaseAuth mAuth;
    //AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        /**et_nombre = (EditText) findViewById(R.id.editTextTextPersonName2);
        et_apellido = (EditText)findViewById(R.id.editTextTextPersonName3);
        et_username = (EditText)findViewById(R.id.editTextTextPersonName4);**/
        email = (EditText)findViewById(R.id.editTextTextPersonName5);
        password = (EditText)findViewById(R.id.passtv);
        passconfirm = (EditText)findViewById(R.id.passconfimr);

        //btn_registro = (Button)findViewById(R.id.btnRegistro);

        mAuth = FirebaseAuth.getInstance();




        /**awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.editTextTextPersonName2, "[a-zA-Z\\s]+",R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.editTextTextPersonName3, "[a-zA-Z\\s]+",R.string.invalid_lastname);
        awesomeValidation.addValidation(this, R.id.editTextTextPersonName4, "[a-zA-Z0-9_-]+",R.string.invalid_username);
        awesomeValidation.addValidation(this, R.id.editTextTextPersonName5, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.passtv, ".{6,}",R.string.invalid_password);

         awesomeValidation.addValidation(RegisterActivity.this, R.id.spinner, new CustomValidation() {
        @Override
            public boolean compare(ValidationHolder validationHolder) {
            if (((Spinner) validationHolder.getView()).getSelectedItem().toString().equals("< Por favor escoge una opción >")) {
            return false;
                }
                    else {
                    return true;
                     }
                }
            }, new CustomValidationCallback() {
        @Override
            public void execute(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(validationHolder.getErrMsg());
                textViewError.setTextColor(Color.RED);
            }
        }, new CustomErrorReset() {
        @Override
            public void reset(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(null);
                textViewError.setTextColor(Color.BLACK);
            }
        }, R.string.err_tech_stacks);

         findViewById(R.id.btn_registro).setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                mAwesomeValidation.validate();
                }
                });

         //OTRA VIA DE ACCESO
        Spinner sp_spinner = (Spinner) findViewById(R.id.spinner);
        sp_spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,genre);
        sp_spinner.setAdapter(aa);

                                            //REGISTRO COMPLETO DE USUARIO - NAVDRAWER ACCESS
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = et_nombre.getText().toString();
                String apellido = et_apellido.getText().toString();
                String nombreuser = et_username.getText().toString();
                String mail = et_email.getText().toString();
                String passw = et_password.getText().toString();

                if(awesomeValidation.validate()){
                    firebaseAuth.createUserWithEmailAndPassword(mail,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{

                            }

                        }
                    })

                }
                else{
                    Toast.makeText(RegisterActivity.this, "Por favor llena los campos para ingresar ;)", Toast.LENGTH_SHORT).show();
                }


                Intent RegIntent = new Intent(RegisterActivity.this, NavDrawer.class);
                startActivity(RegIntent);
            }
        });**/

    }                                             //REGISTRO COMPLETO DE USUARIO - NAVDRAWER ACCESS

     public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void Registeruser(View view) {
        if (password.getText().toString().equals(passconfirm.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Usuario creado con éxito :)", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent RegIntent = new Intent(RegisterActivity.this, NavDrawer.class);
                        startActivity(RegIntent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error al registrarse :(", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden :/", Toast.LENGTH_SHORT).show();
        }
    }




     /**                                   //REGISTRO COMPLETO DE USUARIO - NAVDRAWER ACCESS
    public void Registeruser(View regv){
        Intent RegIntent = new Intent(this, NavDrawer.class);
        startActivity(RegIntent);
    }**/


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
