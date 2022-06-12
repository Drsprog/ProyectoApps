package com.example.proyectoapps.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoapps.EditarRecordatorio;
import com.example.proyectoapps.InfoRecComp;
import com.example.proyectoapps.Model.Recor;
import com.example.proyectoapps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdapterRecComp extends RecyclerView.Adapter<AdapterRecComp.ViewHolder> {

    LayoutInflater inflater;
    ArrayList<Recor> model;
    private View.OnClickListener listener;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<Recor> modelOriginal;


    public AdapterRecComp(Context context, ArrayList<Recor> model){
        this.inflater=LayoutInflater.from(context);
        this.model=model;
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        modelOriginal=new ArrayList<>();
        modelOriginal.addAll(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_rec_completados,parent,false);
        return new AdapterRecComp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String tit=model.get(position).getNOT_REC();
        String fechavenc=model.get(position).getFEC_REC();
        String horavenc=model.get(position).getHOR_REC();
        String lugrec=model.get(position).getLUG_REC();
        String iderec=model.get(position).getIDE_REC();

        holder.titulo.setText(tit);
        holder.fecha.setText(fechavenc);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(holder.itemView.getContext(), InfoRecComp.class);
                intent.putExtra("titRec",tit);
                intent.putExtra("fechaRec",fechavenc );
                intent.putExtra("horaRec",horavenc);
                intent.putExtra("lugRec",lugrec);
                intent.putExtra("ideRec",iderec);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.btnComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Usuario")
                        .child(FirebaseAuth.getInstance().getUid()).child("rec_USU")
                        .child(model.get(position).getIDE_REC()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent= new Intent(holder.itemView.getContext(), EditarRecordatorio.class);
                                    model.remove(model.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(view.getContext(), "Recordatorio completado",Toast.LENGTH_SHORT).show();
                                    holder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
            }
        });
    }

    public void filtrado(String txtBuscar){
        int longitud=txtBuscar.length();
        if(longitud==0){
            model.clear();
            model.addAll(modelOriginal);
        }
        else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Recor> collection=model.stream()
                        .filter(i -> i.getNOT_REC()
                                .toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                model.clear();
                model.addAll(collection);
            }
            else {
                model.clear();
                for (Recor r: modelOriginal){
                    if(r.getNOT_REC().toLowerCase().contains(txtBuscar.toLowerCase())){
                        model.add(r);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Button btnComp;
        TextView titulo, fecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnComp=itemView.findViewById(R.id.btnCompletarComp);
            titulo=itemView.findViewById(R.id.tvTituloRecoComp);
            fecha=itemView.findViewById(R.id.tvFecRecComp);

            titulo.setPaintFlags(titulo.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
            fecha.setPaintFlags(fecha.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);

        }
    }
}
