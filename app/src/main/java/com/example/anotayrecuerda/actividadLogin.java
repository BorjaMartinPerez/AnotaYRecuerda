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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class actividadLogin extends AppCompatActivity {
    EditText introducirMail,introducirContrasena;
    Button btnLogin;
    ProgressBar progressBar;
    TextView btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_login);

        introducirMail = findViewById(R.id.introducir_email);
        introducirContrasena = findViewById(R.id.introducir_contrasena);
        btnLogin = findViewById(R.id.btn_crearCuenta);
        progressBar = findViewById(R.id.barra_progreso);
        btnCrearCuenta = findViewById(R.id.btnRegistrarCuenta);

        btnLogin.setOnClickListener((v)-> loginUsuario() );
        btnCrearCuenta.setOnClickListener((v)->startActivity(new Intent(actividadLogin.this,actividadCrearCuenta.class)) );


    }

    void loginUsuario(){
        String mail  = introducirMail.getText().toString();
        String contrasena  = introducirContrasena.getText().toString();

        boolean esValido = validarDatos(mail,contrasena);
        if(!esValido){
            return;
        }
        loginConFirebase(mail,contrasena);
    }
    void loginConFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        cambioDeProgreso(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                cambioDeProgreso(false);
                if(task.isSuccessful()){
                    //login is success
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to mainactivity
                        startActivity(new Intent(actividadLogin.this,MainActivity.class));
                        finish();
                    }else{
                        Utility.showToast(actividadLogin.this,"Email not verified, Please verify your email.");
                    }

                }else{
                    //login failed
                    Utility.showToast(actividadLogin.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void  cambioDeProgreso(boolean progreso){
        if(progreso){
            progressBar.setVisibility(View.VISIBLE);
            btnCrearCuenta.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            btnCrearCuenta.setVisibility(View.VISIBLE);
        }
    }
    boolean validarDatos(String mail,String contrasena){
        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            introducirMail.setError("Email invalido");
            return false;
        }
        if(contrasena.length()<6){
            introducirContrasena.setError("Contraseña incorrecta");
            return false;
        }

        return true;
    }
}

