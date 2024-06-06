package com.example.anotayrecuerda;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btn_anadirNota;
    RecyclerView recyclerView;
    ImageButton btn_menu;
    NotaAdapter notaadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_anadirNota = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recyler_view);
        btn_menu = findViewById(R.id.btn_menu);


        btn_anadirNota.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, actividadDetallesNota.class)));
        btn_menu.setOnClickListener((v) -> mostrarMenu());
        setupRecyclerView();
    }

    void mostrarMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, btn_menu);
        popupMenu.getMenu().add("Salir de la app");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle() == "Salir de la app") {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, actividadLogin.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceParaNotas().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Nota> options = new FirestoreRecyclerOptions.Builder<Nota>()
                .setQuery(query, Nota.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notaadapter = new NotaAdapter(options, this);
        recyclerView.setAdapter(notaadapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        notaadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notaadapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notaadapter.notifyDataSetChanged();
    }
}