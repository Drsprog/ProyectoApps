package com.example.proyectoapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText etCorLog, etConLog;
    Button btnIng, btnCrear, btnRecuLog;
    String cor,con;


    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        etCorLog=findViewById(R.id.etCorreoLog);
        etConLog=findViewById(R.id.etContLog);
        btnIng=findViewById(R.id.btnIngresar);
        btnCrear=findViewById(R.id.btnCrearCuenta);
        btnRecuLog=findViewById(R.id.btnRecLog);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(Login.this, Registro.class);
                startActivity(next);
            }
        });

        btnIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cor=etCorLog.getText().toString();
                con=etConLog.getText().toString();
                if(!cor.isEmpty()&&!con.isEmpty()){
                    validacion();
                }
            }
        });

        btnRecuLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String opc= "Recuperar contraseña";
                Intent band= new Intent(Login.this,Recuperar_cambiar.class);
                band.putExtra("band", opc);
                startActivity(band);
            }
        });
    }

    private void limpiarcajas() {
        etCorLog.setText("");
        etConLog.setText("");
    }

    private void validacion() {
        cor = etCorLog.getText().toString();
        con = etConLog.getText().toString();
        if (!cor.isEmpty()&&!con.isEmpty()){
            IniciarSesion();
            limpiarcajas();
        }
        else{
            etCorLog.setError("Required");
            etConLog.setError("Required");
            Toast.makeText(Login.this,"Debe ingresar sus datos",Toast.LENGTH_SHORT).show();
        }
    }

    private void IniciarSesion() {
        firebaseAuth.signInWithEmailAndPassword(cor,con).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Login.this,Menu.class));
                    finish();
                }
                else {
                    Toast.makeText(Login.this,"No se pudo iniciar y compruebe los datos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public void Pasar(View view){
//        Intent next = new Intent(this, Registro.class);
//        startActivity(next);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null){
            String bandera= getIntent().getStringExtra("band");
            if(bandera==null){
                startActivity(new Intent(Login.this,Menu.class));
                finish();
            }
        }
    }
}