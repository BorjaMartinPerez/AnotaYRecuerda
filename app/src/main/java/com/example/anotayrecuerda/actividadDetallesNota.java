package com.example.anotayrecuerda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;


public class actividadDetallesNota extends AppCompatActivity {

    EditText tituloNota, contenidoNota;
    ImageButton btn_guardarnota;
    TextView tituloPaginatxt;
    String titulo, contenido, docId;
    boolean editarNota = false;
    TextView borrarNota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_detalles_nota);

        tituloNota = findViewById(R.id.txt_titulonotas);
        contenidoNota = findViewById(R.id.txt_contenidonota);
        btn_guardarnota = findViewById(R.id.btn_guardarnota);
        tituloPaginatxt = findViewById(R.id.titulo_pagina);
        borrarNota = findViewById(R.id.btn_borrarNota);

        // Recibir la informacion
        titulo = getIntent().getStringExtra("titulo");
        contenido = getIntent().getStringExtra("contenido");
        docId = getIntent().getStringExtra("docId");

        // Agregar logs para depuración
        Log.d("actividadDetallesNota", "Titulo: " + titulo);
        Log.d("actividadDetallesNota", "Contenido: " + contenido);
        Log.d("actividadDetallesNota", "DocId: " + docId);

        if (docId != null && !docId.isEmpty()) {
            editarNota = true;
        }

        tituloNota.setText(titulo);
        contenidoNota.setText(contenido);
        if (editarNota) {
            tituloPaginatxt.setText("Edita tu nota");
            borrarNota.setVisibility(View.VISIBLE);
        }

        btn_guardarnota.setOnClickListener((v) -> guardarNota());
        borrarNota.setOnClickListener((v) -> borrarNotaFirebase());
    }


    void guardarNota() {
        String notaTitulo = tituloNota.getText().toString();
        String notaContenido = contenidoNota.getText().toString();
        if (notaTitulo == null || notaTitulo.isEmpty()) {
            tituloNota.setError("El título no puede estar en blanco");
            return;
        }

        Nota nota = new Nota();
        nota.setTitulo(notaTitulo);
        nota.setContenido(notaContenido);
        nota.setTimestamp(nota.timestamp.now());

        guardarNotaEnFirebase(nota);
    }

    void guardarNotaEnFirebase(Nota nota) {
        DocumentReference documentReference;
        if (editarNota) {
            //update the note
            documentReference = Utility.getCollectionReferenceParaNotas().document(docId);
        } else {
            //create new note
            documentReference = Utility.getCollectionReferenceParaNotas().document();
        }

        documentReference.set(nota).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(actividadDetallesNota.this, "Nota añadida correctamente");
                } else {
                    Utility.showToast(actividadDetallesNota.this, "Fallo al añadir la nota");
                }
            }
        });
    }

    void borrarNotaFirebase() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceParaNotas().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //note is deleted
                    Utility.showToast(actividadDetallesNota.this, "Nota borrada correctamente");
                    finish();
                } else {
                    Utility.showToast(actividadDetallesNota.this, "Fallo al borrar la nota");
                }
            }
        });

    }
}




