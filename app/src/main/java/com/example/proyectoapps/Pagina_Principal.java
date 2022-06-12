package com.example.proyectoapps;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.proyectoapps.Adaptadores.AdapterRecordatorio;
import com.example.proyectoapps.Model.Recor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pagina_Principal extends Fragment implements SearchView.OnQueryTextListener {

    RecyclerView recyclerViewPersonas;
    ArrayList<Recor> listaRecordatorio;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String nota,fecha,hora,lugar,imagen,estado,ide;
    AdapterRecordatorio adapterRecordatorio;
    Calendar actual= Calendar.getInstance();
    Calendar fecRecCalendar=Calendar.getInstance();

    TextView tvVac;

    public Pagina_Principal() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        actualizarEstados();
//        cargarLista();
        super.onCreate(savedInstanceState);
    }

    private void actualizarEstados() {
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        String id= firebaseAuth.getCurrentUser().getUid();

        SimpleDateFormat formato= new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date fechaActual= actual.getTime();
        String strFecActu= formato.format(fechaActual);
        try {
            Date fecActualPar=formato.parse(strFecActu);
            actual.setTime(fecActualPar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        databaseReference.child("Usuario").child(id).child("rec_USU").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    for(DataSnapshot ds: task.getResult().getChildren()){
                        ide= ds.getKey();
                        Recor rec= new Recor();
                        rec.setIDE_REC(ide);

                        nota=ds.child("not_REC").getValue().toString();
                        fecha= ds.child("fec_REC").getValue().toString();
                        hora= ds.child("hor_REC").getValue().toString();
                        estado=ds.child("est_REC").getValue().toString();
                        lugar=ds.child("lug_REC").getValue().toString();

                        rec.setNOT_REC(nota);
                        rec.setFEC_REC(fecha);
                        rec.setHOR_REC(hora);
                        rec.setEST_REC(estado);
                        rec.setLUG_REC(lugar);

                        try {
                            Date fecRecDate=formato.parse(rec.getFEC_REC()+ " " +rec.getHOR_REC());
                            fecRecCalendar.setTime(fecRecDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (actual.equals(fecRecCalendar)&&!rec.getEST_REC().equals("Completado"))
                        {
                            Map<String,Object> DatoMod=new HashMap<>();

                            DatoMod.put("est_REC","Atrasado");
                            databaseReference.child("Usuario").child(id).child("rec_USU")
                                    .child(rec.getIDE_REC()).updateChildren(DatoMod);
                            rec.setEST_REC("Atrasado");
                        }
                        else{
                            if(actual.after(fecRecCalendar)&&!rec.getEST_REC().equals("Completado")){
                                Map<String,Object> DatoMod=new HashMap<>();

                                DatoMod.put("est_REC","Atrasado");
                                databaseReference.child("Usuario").child(id).child("rec_USU")
                                        .child(rec.getIDE_REC()).updateChildren(DatoMod);
                                rec.setEST_REC("Atrasado");
                            }
                        }
                        if (rec.getEST_REC().equals("Pendiente"))
                        {
                            listaRecordatorio.add(new Recor(rec.getNOT_REC(),rec.getFEC_REC(),rec.getHOR_REC(),rec.getLUG_REC(), rec.getURL_IMG(), rec.getEST_REC(), rec.getIDE_REC()));
                            recyclerViewPersonas.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapterRecordatorio= new AdapterRecordatorio(getContext(),listaRecordatorio);
                            adapterRecordatorio.notifyDataSetChanged();
                            recyclerViewPersonas.setAdapter(adapterRecordatorio);
                        }
                    }
                    if(recyclerViewPersonas.getAdapter() == null){
                            tvVac.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    tvVac.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pagina__principal,container,false);
        recyclerViewPersonas=view.findViewById(R.id.recycleViewRec);
        listaRecordatorio = new ArrayList<Recor>();
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        tvVac=view.findViewById(R.id.tvVacio);
        return view;
    }

    private void cargarLista() {
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        String id= firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("Usuario").child(id).child("rec_USU").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    for(DataSnapshot ds: task.getResult().getChildren()){
                        ide= ds.getKey();
                        Recor rec= new Recor();
                        rec.setIDE_REC(ide);

                        nota= ds.child("not_REC").getValue().toString();
                        fecha= ds.child("fec_REC").getValue().toString();
                        hora= ds.child("hor_REC").getValue().toString();
                        lugar=ds.child("lug_REC").getValue().toString();
                        imagen=ds.child("img_REC").getValue().toString();
                        estado=ds.child("est_REC").getValue().toString();

                        rec.setNOT_REC(nota);
                        rec.setFEC_REC(fecha);
                        rec.setHOR_REC(hora);
                        rec.setLUG_REC(lugar);
                        rec.setURL_IMG(imagen);
                        rec.setEST_REC(estado);

                        if (rec.getEST_REC().equals("Pendiente"))
                        {
                            listaRecordatorio.add(new Recor(rec.getNOT_REC(),rec.getFEC_REC(),rec.getHOR_REC(),rec.getLUG_REC(), rec.getURL_IMG(), rec.getEST_REC(), rec.getIDE_REC()));
                            recyclerViewPersonas.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapterRecordatorio= new AdapterRecordatorio(getContext(),listaRecordatorio);
                            adapterRecordatorio.notifyDataSetChanged();
                            recyclerViewPersonas.setAdapter(adapterRecordatorio);
                        }
                    }
                }
            }
        });

//        databaseReference.child("Usuario").child(id).child("rec_USU").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot ds: snapshot.getChildren()){
//
//                        ide= ds.getKey();
//                        Recor rec= new Recor();
//
//                        rec.setIDE_REC(ide);
//
//                        nota= ds.child("not_REC").getValue().toString();
//                        fecha= ds.child("fec_REC").getValue().toString();
//                        hora= ds.child("hor_REC").getValue().toString();
//                        lugar=ds.child("lug_REC").getValue().toString();
//                        imagen=ds.child("img_REC").getValue().toString();
//                        estado=ds.child("est_REC").getValue().toString();
//
//                        rec.setNOT_REC(nota);
//                        rec.setFEC_REC(fecha);
//                        rec.setHOR_REC(hora);
//                        rec.setLUG_REC(lugar);
//                        rec.setURL_IMG(imagen);
//                        rec.setEST_REC(estado);
//
//                        listaRecordatorio.add(new Recor(rec.getNOT_REC(),rec.getFEC_REC(),rec.getHOR_REC(),rec.getLUG_REC(), rec.getURL_IMG(), rec.getEST_REC(), rec.getIDE_REC()));
//                        recyclerViewPersonas.setLayoutManager(new LinearLayoutManager(getContext()));
//                        adapterRecordatorio= new AdapterRecordatorio(getContext(),listaRecordatorio);
//                        adapterRecordatorio.notifyDataSetChanged();
//                        recyclerViewPersonas.setAdapter(adapterRecordatorio);
//
//                    }
//                }
//                else{
//                    tvVac.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//       listaRecordatorio.add(new Recor("Boda de primo","2022-08-12","22:00","Av. Girasoles","null"));
//        listaRecordatorio.add(new Recor("Boda del amigo","2022-11-21","23:00","Av. Texas","null"));
//        listaRecordatorio.add(new Recor("Fiesta de mi hermano","2022-10-16","18:00","Av. Lopez","null"));
//        listaRecordatorio.add(new Recor("Boda de primo","2022-08-12","22:00","Av. Girasoles","null"));
//        listaRecordatorio.add(new Recor("Boda del amigo","2022-11-21","23:00","Av. Texas","null"));
//        listaRecordatorio.add(new Recor("Fiesta de mi hermano","2022-10-16","18:00","Av. Lopez","null"));
//        listaRecordatorio.add(new Recor("Boda de primo","2022-08-12","22:00","Av. Girasoles","null"));
//        listaRecordatorio.add(new Recor("Boda del amigo","2022-11-21","23:00","Av. Texas","null"));
//        listaRecordatorio.add(new Recor("Fiesta de mi hermano","2022-10-16","18:00","Av. Lopez","null"));
//        listaRecordatorio.add(new Recor("Boda de primo","2022-08-12","22:00","Av. Girasoles","null"));
//        listaRecordatorio.add(new Recor("Boda del amigo","2022-11-21","23:00","Av. Texas","null"));
//        listaRecordatorio.add(new Recor("Fiesta de mi hermano","2022-10-16","18:00","Av. Lopez","null"));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.busqueda_menu,menu);
        MenuItem searchItem= menu.findItem(R.id.btnBuscarRec);
        SearchView searchView=(SearchView)searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Buscar...");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterRecordatorio.filtrado(newText);
        return false;
    }
}