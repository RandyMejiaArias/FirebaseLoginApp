package com.example.firebaselogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    public static final String user = "name";
    TextView textUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        textUser = (TextView) findViewById(R.id.txtUser);
        String user = getIntent().getStringExtra("name");
        textUser.setText("¡Bienvenido " + user + "!");
    }
}
