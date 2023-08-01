package com.example.tplsuceava.Conectare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tplsuceava.HomeActivity;
import com.example.tplsuceava.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText mEmail, mParola;
    Button mConectareBtn;
    TextView mCreareBtn;

    FirebaseAuth fAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mEmail = findViewById(R.id.email);
        mParola = findViewById(R.id.parola);
        mConectareBtn = findViewById(R.id.conectareBtn);
        mCreareBtn = findViewById(R.id.crearecont);
        fAuth = FirebaseAuth.getInstance();
        mCreareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
        mConectareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String parola=mParola.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Introduceti Email-ul");
                    return;
                }
                if (TextUtils.isEmpty(parola)){
                    mParola.setError("Introduceti Parola");
                    return;
                }
                if (parola.length()<6){
                    mParola.setError("Parola trebuie sa fie mai lunga de 6 caractere");
                    return;
                }
                fAuth.signInWithEmailAndPassword(email, parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Conectare Reusita", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "eroare"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}