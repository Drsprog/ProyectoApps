package com.example.proyectoapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoapps.Model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Recuperar_cambiar extends AppCompatActivity {

    EditText etCorreoR;
    Button btnRec;
    String email="";
    TextView tvTit;
    ProgressDialog progressDialog;
    String nom,ape,con,cor;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_cambiar);

        etCorreoR=findViewById(R.id.etRecuperar);
        btnRec=findViewById(R.id.btnRecuperar);
        tvTit=findViewById(R.id.tvTitRec);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        String Titulo = getIntent().getStringExtra("band");
        tvTit.setText(Titulo);

        progressDialog= new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=etCorreoR.getText().toString();

                if(email.isEmpty()){
                    RecuperarContraseña();
                }
                else{
                    etCorreoR.setError("Required");
                    Toast.makeText(Recuperar_cambiar.this,"Debe ingresar su correo",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getUpdateInfo(String id){

        databaseReference.child("Usuario").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Usuario u = new Usuario();
                    u.setIDE_USU(firebaseAuth.getCurrentUser().getUid());
                    u.setNOM_USU(snapshot.child("nom_USU").getValue().toString());
                    u.setAPE_USU(snapshot.child("ape_USU").getValue().toString());
                    u.setCOR_USU(snapshot.child("cor_USU").getValue().toString());
                    u.setCON_USU(snapshot.child("con_USU").getValue().toString());

                    databaseReference.child("Usuario").child(u.getIDE_USU()).setValue(u);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void RecuperarContraseña() {
        String id= firebaseAuth.getCurrentUser().getUid();
        firebaseAuth.setLanguageCode("es");

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             if (task.isSuccessful()){

                 progressDialog.setMessage("Espere un momento...");
                 progressDialog.setCanceledOnTouchOutside(false);
                 progressDialog.show();


                 Toast.makeText(Recuperar_cambiar.this,"Se ha enviado un correo para restablecer su contraseña",Toast.LENGTH_SHORT).show();
             }
             else{
                 Toast.makeText(Recuperar_cambiar.this,"No se puedo enviar el correo para reestablecer contraseña",Toast.LENGTH_SHORT).show();
             }

             progressDialog.dismiss();
            }
        });

        getUpdateInfo(id);
    }
}