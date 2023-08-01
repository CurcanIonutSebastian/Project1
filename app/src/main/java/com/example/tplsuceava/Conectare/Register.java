package com.example.tplsuceava.Conectare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tplsuceava.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mEmail, mParola, mParola1;
    Button mInregistrareBtn;
    TextView mConectareBtn;

    FirebaseFirestore fstore;
    String userID;
    FirebaseAuth fAuth;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        mEmail = findViewById(R.id.email);
        mParola = findViewById(R.id.parola);
        mParola1 = findViewById(R.id.confirmaParola);
        mInregistrareBtn = findViewById(R.id.inregistrareBtn);
        mConectareBtn = findViewById(R.id.conectareCont);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        mConectareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        mInregistrareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String email = mEmail.getText().toString().trim();
                String parola = mParola.getText().toString().trim();
                String parola1 = mParola1.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Nu ati introdus Email-ul");
                    return;
                }
                if (TextUtils.isEmpty(parola)){
                    mParola.setError("Nu ati introdus parola");
                    return;
                }
                if (parola.length() < 6){
                    mParola.setError("Parola trebuie sa fie mai mare de 6 caractere");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email, parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Inregistrare Reusita", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Eroare: Email-ul nu a fost trimis" + e.getMessage());
                                }
                            });
                            Toast.makeText(getApplicationContext(), "Utilizator Creat", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("utilizator").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("email", email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Succes: profil de utilizator creat pentru"+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Eroare: "+ e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                        else{
                            Toast.makeText(Register.this, "Eroare" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}