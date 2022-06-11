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
        Calendar calendario=Calendar.getInstance();
        int anio= calendario.get(Calendar.YEAR);
        int mes= calendario.get(Calendar.MONTH);
        int dia= calendario.get(Calendar.DAY_OF_MONTH);

        if(view.getId()==R.id.switchFechaMod){

            if(swFec.isChecked()){

                DatePickerDialog dpd= new DatePickerDialog(ModificarInfoRec.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                        if(month<=8){
                            if(day_of_month<=9){
                                String fechaCal="0" + day_of_month + "/0" + (month+1) + "/" + year;
                                tvFecMod.setText(fechaCal);
                            }
                            else{
                                String fechaCal= day_of_month + "/0" + (month+1) + "/" + year;
                                tvFecMod.setText(fechaCal);
                            }
                        }
                        else{
                            if(day_of_month<=9){
                                String fechaCal="0" + day_of_month + "/" + (month+1) + "/" + year;
                                tvFecMod.setText(fechaCal);
                            }
                            else{
                                String fechaCal= day_of_month + "/" + (month+1) + "/" + year;
                                tvFecMod.setText(fechaCal);
                            }
                        }
                    }
                }, anio,mes,dia);
                dpd.show();
            }
            else{
                anio=0;
                mes=0;
                dia=0;
//              String anio1=String.valueOf(anio);
//              String mes1=String.valueOf(mes);
//              String dia1=String.valueOf(dia);
//              tvFec.setText(anio1+mes1+dia1);
                tvFecMod.setText("");
                tvFecMod.setHint("No asignado");
            }
        }
    }

    public void AgregarHora(View view){
        Calendar calendario=Calendar.getInstance();
        int hora= calendario.get(Calendar.HOUR_OF_DAY);
        int min= calendario.get(Calendar.MINUTE);
        if(view.getId()==R.id.switchHoraMod){
            if(swHor.isChecked()){
                TimePickerDialog tmd= new TimePickerDialog(ModificarInfoRec.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour_of_day, int minute) {
                        if(hour_of_day<=9){
                            if(minute<=9){
                                String fechaHor= "0" + hour_of_day + ":0" + minute ;
                                tvHorMod.setText(fechaHor);
                            }
                            else{
                                String fechaHor= "0" + hour_of_day + ":" + minute ;
                                tvHorMod.setText(fechaHor);
                            }
                        }
                        else{
                            if(minute<=9){
                                String fechaHor= hour_of_day + ":0" + minute ;
                                tvHorMod.setText(fechaHor);
                            }
                            else{
                                String fechaHor= hour_of_day + ":" + minute ;
                                tvHorMod.setText(fechaHor);
                            }
                        }
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