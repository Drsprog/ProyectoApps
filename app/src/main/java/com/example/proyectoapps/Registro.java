package com.example.proyectoapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoapps.Model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Registro extends AppCompatActivity {

    private List<Usuario> listUsuario= new ArrayList<Usuario>();
    ArrayAdapter<Usuario> arrayAdapterUsuario;

    TextView txTitulo;
    EditText etNom, etApe, etCor, etCon;
    ListView lstCuentas;
    Button btnRegis;

    String nom,ape,con,cor;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        txTitulo=findViewById(R.id.txtTitulo);
        SpannableString Subtitulo= new SpannableString("REGISTRARSE");
        Subtitulo.setSpan(new UnderlineSpan(),0,Subtitulo.length(),0);
        txTitulo.setText(Subtitulo);

        etNom=findViewById(R.id.etNombres);
        etApe=findViewById(R.id.etApellidos);
        etCor=findViewById(R.id.etCorreo);
        etCon=findViewById(R.id.etContraseña);
        btnRegis= findViewById(R.id.btnRegistrar);

        lstCuentas=findViewById(R.id.lvCuentas);
        lstCuentas.setSelector(R.color.black);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nom = etNom.getText().toString();
                ape = etApe.getText().toString();
                cor = etCor.getText().toString();
                con = etCon.getText().toString();

                validacion();
            }
        });


    }


//    public void Registrar(View view){
//        nom = etNom.getText().toString();
//        ape = etApe.getText().toString();
//        cor = etCor.getText().toString();
//        con = etCon.getText().toString();
//        if (nom.equals("")||ape.equals("")||cor.equals("")||con.equals("")){
//            validacion();
//        }
//        else {
//            Usuario u= new Usuario();
//            u.setIDE_USU(UUID.randomUUID().toString());
//            u.setNOM_USU(nom);
//            u.setAPE_USU(ape);
//            u.setCOR_USU(cor);
//            u.setCON_USU(con);
//            databaseReference.child("Usuario").child(u.getIDE_USU()).setValue(u);
//            limpiarcajas();
//        }
//    }
//
    private void limpiarcajas() {
        etNom.setText("");
        etApe.setText("");
        etCor.setText("");
        etCon.setText("");
    }

    private void validacion() {
        nom = etNom.getText().toString();
        ape = etApe.getText().toString();
        cor = etCor.getText().toString();
        con = etCon.getText().toString();
        if (!nom.isEmpty()&&!ape.isEmpty()&&!cor.isEmpty()&&!con.isEmpty()){
            if (con.length() >= 6) {
                RegistarCuenta();
                limpiarcajas();
            }
            else{
                Toast.makeText(Registro.this,"La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            etNom.setError("Required");
            etApe.setError("Required");
            etCor.setError("Required");
            etCon.setError("Required");
            Toast.makeText(Registro.this,"Debe completar los datos",Toast.LENGTH_SHORT).show();
        }
    }

    private void RegistarCuenta() {
        firebaseAuth.createUserWithEmailAndPassword(cor,con).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Usuario u = new Usuario();
                    u.setIDE_USU(firebaseAuth.getCurrentUser().getUid());
                    u.setNOM_USU(nom);
                    u.setAPE_USU(ape);
                    u.setCOR_USU(cor);
                    u.setCON_USU(con);

                    databaseReference.child("Usuario").child(u.getIDE_USU()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                startActivity(new Intent(Registro.this,MenuPrincipal.class));
                                finish();
                            }
                            else{
                                Toast.makeText(Registro.this,"No se pudieron crear los datos correctamente",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Registro.this,"No se pudo registrar al usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}