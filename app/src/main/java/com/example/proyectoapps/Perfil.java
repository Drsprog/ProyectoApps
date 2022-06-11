package com.example.proyectoapps;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectoapps.Model.Usuario;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    TextView tvNomb, tvApe, tvCor;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    String nom,ape,cor,con,url;

    Uri selectedImage;
    String myUri;
    StorageTask uploadTask;
    CircleImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();

        Toolbar toolbarP=findViewById(R.id.toolbarPerfil);
        Button btnCerrarS=findViewById(R.id.btnCerrarSesion);
        tvNomb=findViewById(R.id.tvNombre);
        tvApe=findViewById(R.id.tvApellidos);
        tvCor= findViewById(R.id.tvCorreo);

        imgView =findViewById(R.id.imgPerFot);
        Button btnRegresar=findViewById(R.id.btnVolverP);
        Button btnMod=findViewById(R.id.btnModificarPer);
        Button btnCamCon=findViewById(R.id.btnCambiarCon);
        Button btnSubFot=findViewById(R.id.btnCambiarP);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Cargando perfil...");
        progressDialog.setCancelable(false);


        cargarFoto();

        setTitle("");

        setSupportActionBar(toolbarP);
        getUserInfo();

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent();
               intent.setAction(Intent.ACTION_GET_CONTENT);
               intent.setType("image/*");
               startActivityForResult(intent,45);
            }
        });

        btnSubFot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if(selectedImage!=null){
                    StorageReference reference=storageReference.child("Usuario").child(firebaseAuth.getCurrentUser().getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                          if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageURL= uri.toString();
                                    String id= firebaseAuth.getCurrentUser().getUid();
                                    databaseReference.child("Usuario").child(id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                nom= snapshot.child("nom_USU").getValue().toString();
                                                ape= snapshot.child("ape_USU").getValue().toString();
                                                cor= snapshot.child("cor_USU").getValue().toString();
                                                con=snapshot.child("con_USU").getValue().toString();
                                                Usuario u= new Usuario();
                                                u.setIDE_USU(id);
                                                u.setNOM_USU(nom);
                                                u.setAPE_USU(ape);
                                                u.setCOR_USU(cor);
                                                u.setCON_USU(con);
                                                u.setURL_IMG(imageURL);
                                                databaseReference.child("Usuario").child(id).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(Perfil.this,"Se subio exitosamente la imagen ",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            });

                          }
                        }
                    });
                }
                else{
                    String id= firebaseAuth.getCurrentUser().getUid();
                    databaseReference.child("Usuario").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                nom= snapshot.child("nom_USU").getValue().toString();
                                ape= snapshot.child("ape_USU").getValue().toString();
                                cor= snapshot.child("cor_USU").getValue().toString();
                                con=snapshot.child("con_USU").getValue().toString();
                                url=snapshot.child("url_USU").getValue().toString();
                                Usuario u= new Usuario();
                                u.setIDE_USU(id);
                                u.setNOM_USU(nom);
                                u.setAPE_USU(ape);
                                u.setCOR_USU(cor);
                                u.setCON_USU(con);
                                u.setURL_IMG(url);
                                databaseReference.child("Usuario").child(id).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Perfil.this,"Se subio exitosamente la imagen ",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        btnCamCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String opc= "Cambiar contraseña";
                Intent band= new Intent(Perfil.this,Recuperar_cambiar.class);
                band.putExtra("band", opc);
                startActivity(band);
            }
        });

        btnCerrarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(Perfil.this,Login.class));
                finish();
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Perfil.this, com.example.proyectoapps.Menu.class);
                startActivity(i);
            }
        });


        btnMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Perfil.this, ModificarPerfil.class);
                startActivity(i);
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
                    if (url.isEmpty()){
                        Context c = getApplicationContext();
                        int id = c.getResources().getIdentifier("ic_baseline_account_circle_24", "drawable", c.getPackageName());
                        imgView.setImageResource(id);

                    }
                    else{
                        Glide.with(imgView.getContext())
                                .load(url)
                                .centerCrop()
                                .into(imgView);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null){
             if(data.getData()!=null){
                 Uri uri=data.getData();
                 long time = new Date().getTime();
                 StorageReference reference=storageReference.child("Usuario").child(time+"");
                 reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filepath=uri.toString();
                                    HashMap<String,Object> obj= new HashMap<>();
                                    obj.put("url_IMG",filepath);
                                    databaseReference.child("Usuario").child(firebaseAuth.getCurrentUser().getUid()).updateChildren(obj);
                                }
                            });
                        }
                     }
                 });
//                imgView.setImageURI(data.getData());
//                selectedImage=data.getData();
             }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==GALLERY_INTENT&&resultCode==RESULT_OK){
//
//            Uri uri=data.getData();
//            StorageReference filepath=storageReference.child("FotosPerfil").child(uri.getLastPathSegment());
//            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Perfil.this,"Se subio exitosamente la imagen ",Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }

    public void getUserInfo(){
        String id= firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("Usuario").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nombre= snapshot.child("nom_USU").getValue().toString();
                    String apellidos= snapshot.child("ape_USU").getValue().toString();
                    String correo= snapshot.child("cor_USU").getValue().toString();

                    tvNomb.setText(nombre);
                    tvApe.setText(apellidos);
                    tvCor.setText(correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}