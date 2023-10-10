package com.example.tplsuceava.connect_classes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tplsuceava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final String TAG = "TAG";
    EditText mEmail, mPassword, mConfirmPassword;
    Button mRegisterBtn;
    TextView mConnectBtn;

    FirebaseFirestore fStore;
    String userID;
    FirebaseAuth fAuth;

    private String email;
    private String password;
    private String confirmPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.passwordR);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        mRegisterBtn = findViewById(R.id.inregistrareBtn);
        mConnectBtn = findViewById(R.id.conectareCont);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mConnectBtn.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Login.class))
        );

        mRegisterBtn.setOnClickListener(v -> {
            email = mEmail.getText().toString().trim();
            password = mPassword.getText().toString().trim();
            confirmPassword = mConfirmPassword.getText().toString().trim();

            if (email.isEmpty()) {
                mEmail.setError("Email!");
                return;
            }

            if (password.isEmpty()) {
                mPassword.setError("Password!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                mConfirmPassword.setError("Incorrect password!");
                return;
            }

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser fUser = fAuth.getCurrentUser();
                    fUser.sendEmailVerification().addOnSuccessListener(unused ->
                            Toast.makeText(getApplicationContext(), "Successful!...", Toast.LENGTH_SHORT).show()
                    ).addOnFailureListener(e ->
                            Log.d(TAG, "Eroare: Email-ul nu a fost trimis" + e.getMessage())
                    );

                    Toast.makeText(getApplicationContext(), "User created!", Toast.LENGTH_SHORT).show();

                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("user").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", email);
                    documentReference.set(user).addOnSuccessListener(unused ->
                            Log.d(TAG, "Succes: profil de utilizator creat pentru" + userID)
                    ).addOnFailureListener(e ->
                            Log.d(TAG, "Error: " + e)
                    );
                    startActivity(new Intent(getApplicationContext(), Login.class));
                } else {
                    Toast.makeText(Register.this, "Eroare" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}