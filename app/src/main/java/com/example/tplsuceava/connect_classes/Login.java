package com.example.tplsuceava.connect_classes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tplsuceava.HomeActivity;
import com.example.tplsuceava.R;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mConnectBtn;
    TextView mCreateBtn;
    FirebaseAuth fAuth;
    private String email;
    private String password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.passwordL);
        mConnectBtn = findViewById(R.id.conectareBtn);
        mCreateBtn = findViewById(R.id.crearecont);
        fAuth = FirebaseAuth.getInstance();

        mCreateBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Register.class)));

        mConnectBtn.setOnClickListener(v -> {
            email = mEmail.getText().toString().trim();
            password = mPassword.getText().toString().trim();

            if (email.isEmpty()) {
                mEmail.setError("Email adress!");
                return;
            }
            if (password.isEmpty()) {
                mPassword.setError("Password!");
                return;
            }

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}