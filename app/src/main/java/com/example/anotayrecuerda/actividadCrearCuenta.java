package com.example.anotayrecuerda;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class actividadCrearCuenta extends AppCompatActivity {
    EditText introducirMail, introducirContrasena, confirmarContrasena;
    Button btnCrearCuenta;
    ProgressBar progressBar;
    TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_crear_cuenta);

        introducirMail = findViewById(R.id.introducir_email);
        introducirContrasena = findViewById(R.id.introducir_contrasena);
        confirmarContrasena = findViewById(R.id.confirmar_contrasena);
        btnCrearCuenta = findViewById(R.id.btn_crearCuenta);
        progressBar = findViewById(R.id.barra_progreso);
        btnLogin = findViewById(R.id.btn_loginCuenta);

        btnCrearCuenta.setOnClickListener(v -> crearCuenta());
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(actividadCrearCuenta.this, actividadLogin.class);
            startActivity(intent);
        });

    }

    void crearCuenta() {
        String mail = introducirMail.getText().toString();
        String contrasena = introducirContrasena.getText().toString();
        String confirmContrasena = confirmarContrasena.getText().toString();

        boolean esValido = validarDatos(mail, contrasena, confirmContrasena);
        if (!esValido) {
            return;
        }
        crearCuentaConFirebase(mail, contrasena);
    }

    void crearCuentaConFirebase(String mail, String contrasena) {
        cambioDeProgreso(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(mail, contrasena).addOnCompleteListener(actividadCrearCuenta.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        cambioDeProgreso(false);
                        if (task.isSuccessful()) {
                            //creating acc is done
                            Toast.makeText(actividadCrearCuenta.this, "Creación de cuenta completada, compruebe el correo para verificar", Toast.LENGTH_SHORT);
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            //failure
                            Toast.makeText(actividadCrearCuenta.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                }
        );
    }

    void cambioDeProgreso(boolean progreso) {
        if (progreso) {
            progressBar.setVisibility(View.VISIBLE);
            btnCrearCuenta.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnCrearCuenta.setVisibility(View.VISIBLE);
        }
    }

    boolean validarDatos(String mail, String contrasena, String confirmContrasena) {
        //validate the data that are input by user.

        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            introducirMail.setError("Email invalido");
            return false;
        }
        if (contrasena.length() < 6) {
            introducirContrasena.setError("Longitud de contraseña no valida");
            return false;
        }
        if (!contrasena.equals(confirmContrasena)) {
            confirmarContrasena.setError("Las contraseñas no coinciden");
            return false;
        }
        return true;
    }
}

