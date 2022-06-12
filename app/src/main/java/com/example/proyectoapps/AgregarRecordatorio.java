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
import android.widget.Toast;

import com.example.proyectoapps.Model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AgregarRecordatorio extends AppCompatActivity {

    Button btnCan,btnAgre;
    TextView tvFec,tvHor;
    EditText etLug,etNot;
    Switch swHor,swFec,swLugar;
    String not,fec,hor,lug;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Calendar calendario=Calendar.getInstance();
    Calendar actual=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_recordatorio);

        btnAgre=findViewById(R.id.btnGuardar);
        btnCan=findViewById(R.id.btnCancelar);
        etNot=findViewById(R.id.etNota);
        tvFec=findViewById(R.id.tvFecha);
        tvHor=findViewById(R.id.tvHora);
        etLug=findViewById(R.id.etLugar);
        swFec=findViewById(R.id.switchFecha);
        swHor=findViewById(R.id.switchHora);
        swLugar=findViewById(R.id.switchLugar);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();


        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),Menu.class);
                startActivity(i);
            }
        });

        btnAgre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistarRecordatorio();
            }
        });

    }

    private void RegistarRecordatorio() {
        String id= firebaseAuth.getCurrentUser().getUid();
        not = etNot.getText().toString();
        fec = tvFec.getText().toString();
        hor = tvHor.getText().toString();
        lug = etLug.getText().toString();


        Map<String,Object> DatoMod=new HashMap<>();

        DatoMod.put("not_REC",not);
        DatoMod.put("fec_REC",fec);
        DatoMod.put("hor_REC",hor);
        DatoMod.put("lug_REC",lug);
        DatoMod.put("img_REC","");
        DatoMod.put("est_REC","Pendiente");

        databaseReference.child("Usuario").child(id).child("rec_USU").push().setValue(DatoMod).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent i= new Intent(AgregarRecordatorio.this,Menu.class);
                startActivity(i);
                finish();
            }
        });
        }

    public void AgregarFecha(View view){

        int anio= actual.get(Calendar.YEAR);
        int mes= actual.get(Calendar.MONTH);
        int dia= actual.get(Calendar.DAY_OF_MONTH);

        if(view.getId()==R.id.switchFecha){

            if(swFec.isChecked()){

                DatePickerDialog dpd= new DatePickerDialog(AgregarRecordatorio.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {

                        calendario.set(Calendar.DAY_OF_MONTH,day_of_month);
                        calendario.set(Calendar.MONTH,month);
                        calendario.set(Calendar.YEAR,year);

                        SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
                        String fechaCal= format.format(calendario.getTime());
                        tvFec.setText(fechaCal);
                    }
                }, anio,mes,dia);
                dpd.show();
            }
            else{
                anio=0;
                mes=0;
                dia=0;
                tvFec.setText("");
                tvFec.setHint("No asignado");
            }
        }
    }

    public void AgregarHora(View view){
        int hora= actual.get(Calendar.HOUR_OF_DAY);
        int min= actual.get(Calendar.MINUTE);
        if(view.getId()==R.id.switchHora){
            if(swHor.isChecked()){
                TimePickerDialog tmd= new TimePickerDialog(AgregarRecordatorio.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour_of_day, int minute) {

                        calendario.set(Calendar.HOUR_OF_DAY,hour_of_day);
                        calendario.set(Calendar.MINUTE,minute);

                        tvHor.setText(String.format("%02d:%02d",hour_of_day,minute));

                    }
                },hora,min,false);
                tmd.show();
            }
            else{
                hora=0;
                min=0;
                tvHor.setText("");
                tvHor.setHint("No asignado");
            }
        }
    }

    public void AgregarLugar(View view){
        if(view.getId()==R.id.switchLugar){
            if(swLugar.isChecked()){
                etLug.setVisibility(View.VISIBLE);
            }
            else{
                etLug.setVisibility(View.INVISIBLE);
            }
        }
    }
}