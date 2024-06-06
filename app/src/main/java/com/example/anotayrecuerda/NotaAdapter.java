package com.example.anotayrecuerda;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotaAdapter extends FirestoreRecyclerAdapter<Nota, NotaAdapter.NotaViewHolder> {
    Context context;

    public NotaAdapter(@NonNull FirestoreRecyclerOptions<Nota> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotaViewHolder holder, int position, @NonNull Nota nota) {
        holder.txtTitulo.setText(nota.titulo);
        holder.txtContenido.setText(nota.contenido);
        holder.txtTimestamp.setText(Utility.timestampToString(nota.timestamp));

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,actividadDetallesNota.class);
            intent.putExtra("titulo",nota.titulo);
            intent.putExtra("contenido",nota.contenido);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_nota_item,parent,false);
        return new NotaViewHolder(view);
    }

    class NotaViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitulo,txtContenido,txtTimestamp;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txt_titulo);
            txtContenido = itemView.findViewById(R.id.txt_contenido);
            txtTimestamp = itemView.findViewById(R.id.nota_timestamp);
        }
    }
}