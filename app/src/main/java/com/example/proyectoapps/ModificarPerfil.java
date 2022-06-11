package com.example.proyectoapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectoapps.Model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ModificarPerfil extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText etNomb, etApe, etCor;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    String nom,ape,cor,con,url;
    Button btnModData,btnRegP;



    Uri selectedImage;
    String myUri;
    StorageTask uploadTask;
    CircleImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);

        etNomb=findViewById(R.id.etMNombre);
        etApe=findViewById(R.id.etMApellidos);
        etCor=findViewById(R.id.etMCorreo);
        btnModData= findViewById(R.id.btnCambiarDatM);
        imgView =findViewById(R.id.imgPerFotP);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        btnRegP=findViewById(R.id.btnVolverP);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Modicando datos ...");
        progressDialog.setCancelable(false);



        cargarFoto();


        btnRegP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ModificarPerfil.this, Perfil.class);
                startActivity(i);
            }
        });


        btnModData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nom = etNomb.getText().toString();
                ape = etApe.getText().toString();
                cor = etCor.getText().toString();
                if (!nom.isEmpty()&&!ape.isEmpty()&&!cor.isEmpty()){
                        ModificarCuenta();
                        limpiarcajas();
                }
                else{
                    etNomb.setError("Required");
                    etApe.setError("Required");
                    etCor.setError("Required");
                    Toast.makeText(ModificarPerfil.this,"Debe completar los datos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void limpiarcajas() {
        etNomb.setText("");
        etApe.setText("");
        etCor.setText("");
    }

    private void ModificarCuenta() {

        String id= firebaseAuth.getCurrentUser().getUid();
        nom = etNomb.getText().toString();
        ape = etApe.getText().toString();
        cor = etCor.getText().toString();

        Map<String,Object> DatoMod=new HashMap<>();

        DatoMod.put("nom_USU",nom);
        DatoMod.put("ape_USU",ape);
        DatoMod.put("cor_USU",cor);

        databaseReference.child("Usuario").child(id).updateChildren(DatoMod).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if(task2.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updateEmail(cor).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Intent band= new Intent(ModificarPerfil.this,Perfil.class);
                                startActivity(band);
                                Toast.makeText(ModificarPerfil.this, "Correo cambiado" + " El correo actual es: " + cor, Toast.LENGTH_LONG).show();
                                finish();

                            }
                        }
                    });

                }
                else{
                    Toast.makeText(ModificarPerfil.this,"No se pudieron actualizar los datos correctamente",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void cargarFoto() {
        String id= firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("Usuario").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String url= snapshot.child("url_IMG").getValue().toString();

                    Glide.with(imgView.getContext())
                            .load(url)
                            .centerCrop()
                            .into(imgView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}