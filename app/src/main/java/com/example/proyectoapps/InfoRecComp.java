package com.example.proyectoapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class InfoRecComp extends AppCompatActivity {

    TextView tvNotComp, tvFecComp, tvHorComp, tvLugComp;
    String tit,fec,hor,lug,ide,est;
    Button btnCan, btnRest;
    String titDat,fecDat,horDat,lugDat,ideDat;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_rec_comp);

        tvNotComp=findViewById(R.id.tvNotaCompRec);
        tvFecComp=findViewById(R.id.tvFechaCompRec);
        tvHorComp=findViewById(R.id.tvHoraCompRec);
        tvLugComp=findViewById(R.id.tvLugarCompRec);
        btnCan=findViewById(R.id.btnCanRecComp);
        btnRest=findViewById(R.id.btnRestRec);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        tit=getIntent().getStringExtra("titRec");
        fec=getIntent().getStringExtra("fechaRec");
        hor=getIntent().getStringExtra("horaRec");
        lug=getIntent().getStringExtra("lugRec");
        ide=getIntent().getStringExtra("ideRec");

        tvNotComp.setText(tit);
        tvFecComp.setText(fec);
        tvHorComp.setText(hor);
        tvLugComp.setText(lug);

        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),Menu.class);
                startActivity(i);
            }
        });

        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id= firebaseAuth.getCurrentUser().getUid();

                Map<String,Object> DatoMod=new HashMap<>();

                DatoMod.put("est_REC","Pendiente");

                databaseReference.child("Usuario").child(id).child("rec_USU").child(ide).updateChildren(DatoMod).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent i= new Intent(getApplicationContext(),Menu.class);
                            startActivity(i);
                            Toast.makeText(view.getContext(), "Recordatorio restaurado",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });

    }
}