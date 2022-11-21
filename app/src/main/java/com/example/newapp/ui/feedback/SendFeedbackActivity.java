package com.example.newapp.ui.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newapp.R;

public class SendFeedbackActivity extends Activity {
    private EditText editTextTextMultiLine;
    private Button btnEnviarfeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbacksend);

        editTextTextMultiLine = (EditText) findViewById(R.id.editTextTextMultiLine);
        btnEnviarfeedback = (Button) findViewById(R.id.btnEnviarfeedback);
        btnEnviarfeedback.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextTextMultiLine.getText().toString().isEmpty()){
                    Intent intefeedbck = new Intent(SendFeedbackActivity.this, ReceivedfeedbackActivity.class);
                    startActivity(intefeedbck);
                }
                else{
                    Toast.makeText(SendFeedbackActivity.this, "Para enviar un mensaje, primero debes llenar el campo de texto ;)", Toast.LENGTH_SHORT).show();
                }

            }
        }));
    }
}
