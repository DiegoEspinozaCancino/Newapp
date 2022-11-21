package com.example.newapp.SQLLite;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newapp.R;

public class Agregar extends AppCompatActivity {

    private EditText ed_nombre,ed_apellido,ed_telefono, ed_email, ed_password, ed_genero;
    private Button b_agregar,b_ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlliteadd);

        ed_nombre = findViewById(R.id.et_nombre);
        ed_apellido = findViewById(R.id.et_apellido);
        ed_telefono = findViewById(R.id.et_telefono);
        ed_email = findViewById(R.id.et_email);
        ed_password = findViewById(R.id.et_password);
        ed_genero = findViewById(R.id.et_genero);

        b_agregar = findViewById(R.id.btn_agregar);
        b_ver = findViewById(R.id.btn_ver);

        b_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), Leer.class);
                startActivity(i);
            }
        });
        b_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertar();
            }
        });
    }

    public void insertar()
    {
        try
        {
            String nombre = ed_nombre.getText().toString();
            String apellido = ed_apellido.getText().toString();
            String telefono = ed_telefono.getText().toString();
            String email = ed_email.getText().toString();
            String password = ed_password.getText().toString();
            String genero = ed_genero.getText().toString();


            SQLiteDatabase db = openOrCreateDatabase("BD_TEMPORAL", Context.MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS persona(id INTEGER PRIMARY KEY AUTOINCREMENT,nombre VARCHAR,apellido VARCHAR,telefono VARCHAR,email VARCHAR,password VARCHAR,genero VARCHAR)");

            String sql = "insert into persona(nombre,apellido,telefono,email,password,genero)values(?,?,?,?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,nombre);
            statement.bindString(2,apellido);
            statement.bindString(3,telefono);
            statement.bindString(4,email);
            statement.bindString(5,password);
            statement.bindString(6,genero);
            statement.execute();

            Toast.makeText(this,"Registro completado con éxito.",Toast.LENGTH_LONG).show();

            ed_nombre.setText("");
            ed_apellido.setText("");
            ed_telefono.setText("");
            ed_email.setText("");
            ed_password.setText("");
            ed_genero.setText("");
            ed_nombre.requestFocus();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"WHOPS!... Hubo un error al registrarse :/.",Toast.LENGTH_LONG).show();
        }
    }
}
