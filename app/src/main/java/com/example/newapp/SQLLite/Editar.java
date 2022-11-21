package com.example.newapp.SQLLite;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newapp.R;

public class Editar extends AppCompatActivity {

    private EditText ed_nombre,ed_apellido,ed_telefono,ed_email,ed_password,ed_genero,ed_id;
    private Button b_editar,b_eliminar,b_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlliteedit);

        ed_nombre = findViewById(R.id.et_nombre);
        ed_apellido = findViewById(R.id.et_apellido);
        ed_telefono = findViewById(R.id.et_telefono);
        ed_email = findViewById(R.id.et_email);
        ed_password = findViewById(R.id.et_password);
        ed_genero = findViewById(R.id.et_genero);
        ed_id = findViewById(R.id.id);

        b_editar = findViewById(R.id.btn_editar);
        b_eliminar = findViewById(R.id.btn_eliminar);
        b_volver = findViewById(R.id.btn_volver);

        Intent i = getIntent();

        String et_id = i.getStringExtra("id").toString();
        String et_nombre = i.getStringExtra("nombre").toString();
        String et_apellido = i.getStringExtra("apellido").toString();
        String et_telefono = i.getStringExtra("telefono").toString();
        String et_email = i.getStringExtra("email").toString();
        String et_password = i.getStringExtra("password").toString();
        String et_genero = i.getStringExtra("genero").toString();


        ed_id.setText(et_id);
        ed_nombre.setText(et_nombre);
        ed_apellido.setText(et_apellido);
        ed_telefono.setText(et_telefono);
        ed_email.setText(et_email);
        ed_password.setText(et_password);
        ed_genero.setText(et_genero);

        b_editar.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                editar();
            }
        });

        b_eliminar.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                eliminar();
            }
        });

        b_volver.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent i = new Intent(getApplicationContext(), Leer.class);
                startActivity(i);
            }
        });
    }

    public void eliminar()
    {
        try
        {
            String id = ed_id.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("BD_TEMPORAL", Context.MODE_PRIVATE,null);


            String sql = "delete from persona where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1,id);
            statement.execute();
            Toast.makeText(this,"Los datos han sido eliminados de la base de datos.",Toast.LENGTH_LONG).show();

            ed_nombre.setText("");
            ed_apellido.setText("");
            ed_telefono.setText("");
            ed_email.setText("");
            ed_password.setText("");
            ed_password.setText("");
            ed_nombre.requestFocus();

        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Ha surgido un error, no se pudieron guardar los datos.",Toast.LENGTH_LONG).show();
        }
    }
    public void editar()
    {
        try
        {
            String nombre = ed_nombre.getText().toString();
            String apellido = ed_apellido.getText().toString();
            String telefono = ed_telefono.getText().toString();
            String email = ed_email.getText().toString();
            String password = ed_password.getText().toString();
            String genero = ed_genero.getText().toString();
            String id = ed_id.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("BD_TEMPORAL",Context.MODE_PRIVATE,null);

            String sql = "update persona set nombre = ?,apellido=?,edad=? where id= ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,nombre);
            statement.bindString(2,apellido);
            statement.bindString(3,telefono);
            statement.bindString(3,email);
            statement.bindString(3,password);
            statement.bindString(3,genero);
            statement.bindString(4,id);
            statement.execute();
            Toast.makeText(this,"Los datos han sido actualizados satisfactoriamente.",Toast.LENGTH_LONG).show();

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
            Toast.makeText(this,"WHOPS!... No se pudieron guardar los datos.",Toast.LENGTH_LONG).show();
        }
    }
}
