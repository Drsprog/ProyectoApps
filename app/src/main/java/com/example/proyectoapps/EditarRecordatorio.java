package com.example.proyectoapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EditarRecordatorio extends AppCompatActivity {

    TextView tvNotMod, tvFecMod, tvHorMod, tvLugMod;
    String tit,fec,hor,lug,ide;
    Button btnCan, btnMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_recordatorio);

        tvNotMod=findViewById(R.id.tvNotaModRec);
        tvFecMod=findViewById(R.id.tvFechaModRec);
        tvHorMod=findViewById(R.id.tvHoraModRec);
        tvLugMod=findViewById(R.id.tvLugarModRec);
        btnCan=findViewById(R.id.btnCanRecMod);
        btnMod=findViewById(R.id.btnModRec);

        tit=getIntent().getStringExtra("titRec");
        fec=getIntent().getStringExtra("fechaRec");
        hor=getIntent().getStringExtra("horaRec");
        lug=getIntent().getStringExtra("lugRec");
        ide=getIntent().getStringExtra("ideRec");

        tvNotMod.setText(tit);
        tvFecMod.setText(fec);
        tvHorMod.setText(hor);
        tvLugMod.setText(lug);

        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),Menu.class);
                startActivity(i);
                finish();
            }
        });

        btnMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),ModificarInfoRec.class);
                intent.putExtra("titRec",tit);
                intent.putExtra("fechaRec",fec );
                intent.putExtra("horaRec",hor);
                intent.putExtra("lugRec",lug);
                intent.putExtra("ideRec",ide);
                startActivity(intent);
            }
        });

    }
}