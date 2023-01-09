package com.piggybank.vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.piggybank.R;
import com.piggybank.controleur.Controle;
import com.piggybank.modele.Tirelire;
import com.piggybank.outils.NotificationService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentService = new Intent(this, NotificationService.class);
        startService(intentService);
        Intent serviceIntent = new Intent(this, NotificationService.class);
        this.startService(serviceIntent);



        init();
    }

    //Propriétés
    private EditText main_edittext_namePiggybank;
    private Button main_btn_createPiggybank;
    private ListView main_listView_tirelire;
    private Controle controle;

    /**
     * Initialisation des liens avec les objets graphiques
     */
    public void init()
    {
        main_edittext_namePiggybank = findViewById(R.id.main_edittext_namePiggybank);
        main_btn_createPiggybank = findViewById(R.id.main_btn_createPiggybank);
        main_listView_tirelire = findViewById(R.id.main_listView_tirelire);

        this.controle = Controle.getInstance(this);
        ecouteBtnCreatePiggybank();
        ecouteListPiggybank();

        main_btn_createPiggybank.setEnabled(false); //désactivation du bouton si champs vide

        /**
         * Ecoute l'editText
         */
        main_edittext_namePiggybank.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                main_btn_createPiggybank.setEnabled(!main_edittext_namePiggybank.toString().isEmpty());
            }
        });

        //adapte la listview avec les noms des tirelires
        main_listView_tirelire.setAdapter(controle.getAdapterTirelire(this));
    }

    /**
     * ecoute le bouton create Piggybank
     */
    private void ecouteBtnCreatePiggybank()
    {
        findViewById(R.id.main_btn_createPiggybank).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                //Log.d("message", "Bouton creer tirelire cliqué");

                String nom;

                nom = main_edittext_namePiggybank.getText().toString();

                if(nom.equals(""))
                {
                    Toast.makeText(MainActivity.this, R.string.toastSaisieIncorrect, Toast.LENGTH_SHORT).show();
                } else
                {
                    MainActivity.this.controle.creerTirelire(nom);
                    Tirelire tirelire = controle.getDerniereTirelire();
                    Intent intent = new Intent(MainActivity.this, TirelireActivity.class);
                    intent.putExtra("tirelire", tirelire); // la clé et la tirelire créer en dernier
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * ecoute la liste de tirelire
     */
    private void ecouteListPiggybank()
    {
        main_listView_tirelire.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                Tirelire tirelire = controle.getTirelire(position+1); //récupère la tirelire via son ID
                Intent intent = new Intent(MainActivity.this, TirelireActivity.class);
                intent.putExtra("tirelire", tirelire); // la clé et la tirelire cliqué
                startActivity(intent);
            }
        });
    }

}