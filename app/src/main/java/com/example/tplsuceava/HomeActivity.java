package com.example.tplsuceava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.example.tplsuceava.Conectare.MainActivity;
import com.example.tplsuceava.databinding.ActivityHomeBinding;
import com.example.tplsuceava.fragment.MapsFragment;
import com.example.tplsuceava.fragment.NotificationFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        requestWindowFeature (Window. FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        ReplaceFragment(new MapsFragment());

        binding.navBottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        ReplaceFragment(new MapsFragment());
                        break;

                    case R.id.notification:
                        ReplaceFragment(new NotificationFragment());
                        break;
                    default:
                     deconectare();
                        break;
                }
                return true;
            }
        });

    }
    private void ReplaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
    private void deconectare() {

        FirebaseAuth.getInstance().signOut();

        // Redirecționare către pagina de autentificare
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);

    }
}