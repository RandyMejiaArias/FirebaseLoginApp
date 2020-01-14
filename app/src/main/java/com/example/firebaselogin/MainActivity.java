package com.example.firebaselogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Defining view objects
    private EditText textEmail;
    private EditText textPassword;
    private Button buttonSignup;
    private Button buttonLogin;
    private ProgressDialog progressDialog;

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Referenciamos los views
        textEmail = (EditText) findViewById(R.id.txtEmail);
        textPassword = (EditText) findViewById(R.id.txtPassword);
        buttonSignup = (Button) findViewById(R.id.btnSignup);
        buttonLogin = (Button) findViewById(R.id.btnLogin);
        progressDialog = new ProgressDialog(this);

        //Attaching listener to button
        buttonSignup.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    private void SignUp(){
        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacias
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Por favor ingrese la contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Realizando registro...");
        progressDialog.show();

        //Crear un nuevo usuario
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Verificar si se realizó corrrectamente
                        if(task.isSuccessful())
                            Toast.makeText(MainActivity.this,"Se ha registrado el usuario con el email: " + textEmail.getText(), Toast.LENGTH_LONG).show();
                        else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) //Si se presenta una colisión.
                                Toast.makeText(MainActivity.this, "El usuario ya se ha registrado", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MainActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void Login(){
        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacias
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Por favor ingrese la contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        //Iniciar sesion
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Verificar si se realizó corrrectamente
                        if(task.isSuccessful()){
                            int pos = email.indexOf("@");
                            String user = email.substring(0, pos);
                            Toast.makeText(MainActivity.this,"Bienvenido: " + textEmail.getText(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplication(), WelcomeActivity.class);
                            intent.putExtra(WelcomeActivity.user, user);
                            startActivity(intent);
                        }

                        else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) //Si se presenta una colisión.
                                Toast.makeText(MainActivity.this, "El usuario no se ha registrado", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MainActivity.this, "Los datos son incorrectos", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignup:
                SignUp();
                break;
            case R.id.btnLogin:
                Login();

        }

    }
}
