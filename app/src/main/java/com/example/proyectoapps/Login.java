package com.example.proyectoapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText etCorLog, etConLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorLog=findViewById(R.id.etCorreoLog);
        etConLog=findViewById(R.id.etContLog);

    }

    public void Pasar(View view){
        Intent next = new Intent(this, Registro.class);
        startActivity(next);
    }

}