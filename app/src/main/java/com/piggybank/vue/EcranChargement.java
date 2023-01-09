package com.piggybank.vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.piggybank.R;
import com.piggybank.vue.MainActivity;

public class EcranChargement extends AppCompatActivity {
    private final int TEMPS_ECRAN_CHARGEMENT = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecran_chargement);

        //Rediriger vers la page principale apres 4,5 secondes de chargement
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, TEMPS_ECRAN_CHARGEMENT);
    }
}