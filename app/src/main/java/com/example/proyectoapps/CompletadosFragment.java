package com.example.proyectoapps;

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

import com.example.proyectoapps.Adaptadores.AdapterRecComp;
import com.example.proyectoapps.Adaptadores.AdapterRecordatorio;
import com.example.proyectoapps.Model.Recor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class CompletadosFragment extends Fragment implements SearchView.OnQueryTextListener{

    RecyclerView recyclerViewPersonas;
    ArrayList<Recor> listaRecordatorio;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String nota,fecha,hora,lugar,imagen,estado,ide;
    AdapterRecComp adapterRecComp;

    TextView tvVac;

    public CompletadosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        cargarLista();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completados,container,false);
        recyclerViewPersonas=view.findViewById(R.id.recycleViewRecComp);
        listaRecordatorio = new ArrayList<Recor>();
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        tvVac=view.findViewById(R.id.tvVacioComp);

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

                        if(rec.getEST_REC().equals("Completado")){
                            listaRecordatorio.add(new Recor(rec.getNOT_REC(),rec.getFEC_REC(),rec.getHOR_REC(),rec.getLUG_REC(), rec.getURL_IMG(), rec.getEST_REC(), rec.getIDE_REC()));
                            recyclerViewPersonas.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapterRecComp= new AdapterRecComp(getContext(),listaRecordatorio);
                            adapterRecComp.notifyDataSetChanged();
                            recyclerViewPersonas.setAdapter(adapterRecComp);
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
        adapterRecComp.filtrado(newText);
        return false;
    }
}