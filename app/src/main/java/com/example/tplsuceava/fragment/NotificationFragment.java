package com.example.tplsuceava.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.tplsuceava.R;
import com.example.tplsuceava.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;

    public NotificationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        Button line1Button = rootView.findViewById(R.id.line1Button);
        Button line2Button = rootView.findViewById(R.id.line2Button);
        Button line3Button = rootView.findViewById(R.id.line3Button);
        line1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog("Linia 1", "Cinema - Orizont - IRIC - Carrefour- " +
                        "Bazar - Sala Sporturilor - Grup Școlar - Centru - " +
                        "Bancă - Policlinică - Catedrală - Curcubeu - Mobilă - " +
                        "Calea Obcinilor Flori - Întoarcere semafor parc - Calea Obcinilor Flori -  " +
                        "Mobilă - Curcubeu - Nordic - Catedrală  - Policlinică - Bancă - Centru - Grup Școlar - " +
                        "Sala Sporturilor - Bazar - Carrefour - Orizont - Cinema");
            }
        });

        line2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog("Linia 2", "Gara Burdujeni - Comlemn - Cantină - Moldova - Orizont - IRIC - Carrefour - Iulius Mall - Bazar - Sala Sporturilor - Grup Școlar - Centru - Bancă - Policlinică - Spitalul Judeţean - Metro - Corduș - Școala nr. 9 - Obcini Flori - Mobilă - Curcubeu - Nordic - Catedrală - Policlinică - Bancă - Centru - Grup Școlar - Sala Sporturilor - Iulius Mall - Bazar - Carrefour - Orizont - Moldova - Cantină - Ramiro - Cartier ANL - Gara Burdujeni");
            }
        });

        line3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog("Linia 3", "Gara Burdujeni - Comlemn - Cantină - Moldova - Orizont - IRIC - Carrefour - Bazar - Sala Sporturilor - Colegiul „Petru Muşat” - Centru - Bancă - Policlinică - Spitalul Judeţean - Obcini Flori - Pasaj CFR Vest - Centură - Rulmentul - Jumbo - Bermas - Gara Suceava Vest - Mobilă - Curcubeu - Nordic - Catedrală - Policlinică - Bancă - Centru - Colegiul „Petru Muşat” - Sala Sporturilor - Bazar - Carrefour - Orizont - Moldova - Cantina - Ramiro - Cartier ANL - Gara Burdujeni");
            }
        });

        return rootView;
    }

    private void showInfoDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
