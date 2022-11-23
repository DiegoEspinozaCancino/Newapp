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
import com.example.newapp.ui.feedback.ReceivedfeedbackActivity;

public class Agregar extends AppCompatActivity {

    private EditText editTextTextMultiLine;
    private Button btnEnviarfeedback;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbacksend);

        editTextTextMultiLine = (EditText)findViewById(R.id.editTextTextMultiLine);
        btnEnviarfeedback = (Button)findViewById(R.id.btnEnviarfeedback);
        DB = new DBHelper (this);

        btnEnviarfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextTextMultiLine.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(Agregar.this, "Debes llenar el campo de texto para enviar un feedback ;)", Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean insert = DB.insertData(message);
                    if(insert == true){
                        Toast.makeText(Agregar.this, "Mensaje enviado :D", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Agregar.this , ReceivedfeedbackActivity.class);
                        startActivity(i);
                    }
                }
            }
        });

    }
}
