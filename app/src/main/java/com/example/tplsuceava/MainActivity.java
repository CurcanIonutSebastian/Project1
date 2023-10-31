package com.example.tplsuceava;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.tplsuceava.connect_classes.Login;
import com.example.tplsuceava.connect_classes.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button login, register;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            startActivity(new Intent(this, HomeActivity.class));
        }

        login = findViewById(R.id.loginbtn);
        register = findViewById(R.id.registerbtn);

        login.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));

        register.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Register.class)));
    }
}