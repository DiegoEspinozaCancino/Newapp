package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String [] genre = {"Seleccione género", "Masculino", "Femenino", "No binario", "Otros"};
    private EditText ed_nombre, ed_apellido, ed_telefono, ed_email, ed_password, sp_spinner;
    private Button btn_registro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,genre);
        spin.setAdapter(aa);
    }
                                        //REGISTRO COMPLETO DE USUARIO - NAVDRAWER ACCESS
    public void Registeruser(View regv){
        Intent RegIntent = new Intent(this, NavDrawer.class);
        startActivity(RegIntent);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
