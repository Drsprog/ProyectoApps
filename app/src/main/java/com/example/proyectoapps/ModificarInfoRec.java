package com.example.proyectoapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ModificarInfoRec extends AppCompatActivity {

    TextView tvFecMod, tvHorMod;
    EditText etLugMod,etNotMod;
    String tit,fec,hor,lug,ide;
    String titDat,fecDat,horDat,lugDat,ideDat;
    Button btnCan, btnMod;

    Switch swHor,swFec,swLugar;

    Calendar calendario=Calendar.getInstance();
    Calendar actual=Calendar.getInstance();

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_info_rec);

        etNotMod=findViewById(R.id.etNotaMd);
        tvFecMod=findViewById(R.id.tvMdFecha);
        tvHorMod=findViewById(R.id.tvMdHora);
        etLugMod=findViewById(R.id.etMdLugar);
        btnCan=findViewById(R.id.btnCancelarMd);
        btnMod=findViewById(R.id.btnGuarCamb);
        swHor=findViewById(R.id.switchHoraMod);
        swFec=findViewById(R.id.switchFechaMod);
        swLugar=findViewById(R.id.switchLugarMod);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        tit=getIntent().getStringExtra("titRec");
        fec=getIntent().getStringExtra("fechaRec");
        hor=getIntent().getStringExtra("horaRec");
        lug=getIntent().getStringExtra("lugRec");
        ide=getIntent().getStringExtra("ideRec");

        etNotMod.setText(tit);
        tvFecMod.setText(fec);
        tvHorMod.setText(hor);
        etLugMod.setText(lug);

        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),Menu.class);
                startActivity(i);
            }
        });

        btnMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id= firebaseAuth.getCurrentUser().getUid();
                titDat = etNotMod.getText().toString();
                fecDat = tvFecMod.getText().toString();
                horDat = tvHorMod.getText().toString();
                lugDat = etLugMod.getText().toString();


                Map<String,Object> DatoMod=new HashMap<>();

                DatoMod.put("not_REC",titDat);
                DatoMod.put("fec_REC",fecDat);
                DatoMod.put("hor_REC",horDat);
                DatoMod.put("lug_REC",lugDat);
                DatoMod.put("img_REC","");
                DatoMod.put("est_REC","Pendiente");

                databaseReference.child("Usuario").child(id).child("rec_USU").child(ide).updateChildren(DatoMod).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent i= new Intent(getApplicationContext(),Menu.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }
        });
    }

    public void AgregarFecha(View view){
        int anio= actual.get(Calendar.YEAR);
        int mes= actual.get(Calendar.MONTH);
        int dia= actual.get(Calendar.DAY_OF_MONTH);

        if(view.getId()==R.id.switchFechaMod){

            if(swFec.isChecked()){

                DatePickerDialog dpd= new DatePickerDialog(ModificarInfoRec.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                        calendario.set(Calendar.DAY_OF_MONTH,day_of_month);
                        calendario.set(Calendar.MONTH,month);
                        calendario.set(Calendar.YEAR,year);

                        SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
                        String fechaCal= format.format(calendario.getTime());
                        tvFecMod.setText(fechaCal);
                    }
                }, anio,mes,dia);
                dpd.show();
            }
            else{
                anio=0;
                mes=0;
                dia=0;
                tvFecMod.setText("");
                tvFecMod.setHint("No asignado");
            }
        }
    }

    public void AgregarHora(View view){
        int hora= actual.get(Calendar.HOUR_OF_DAY);
        int min= actual.get(Calendar.MINUTE);
        if(view.getId()==R.id.switchHoraMod){
            if(swHor.isChecked()){
                TimePickerDialog tmd= new TimePickerDialog(ModificarInfoRec.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour_of_day, int minute) {
                        calendario.set(Calendar.HOUR_OF_DAY,hour_of_day);
                        calendario.set(Calendar.MINUTE,minute);

                        tvHorMod.setText(String.format("%02d:%02d",hour_of_day,minute));
                    }
                },hora,min,false);
                tmd.show();
            }
            else{
                hora=0;
                min=0;
                tvHorMod.setText("");
                tvHorMod.setHint("No asignado");
            }
        }
    }

    public void AgregarLugar(View view){
        if(view.getId()==R.id.switchLugarMod){
            if(swLugar.isChecked()){
                etLugMod.setVisibility(View.VISIBLE);
            }
            else{
                etLugMod.setVisibility(View.INVISIBLE);
            }
        }
    }
}