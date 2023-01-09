package com.piggybank.vue;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.piggybank.R;
import com.piggybank.controleur.Controle;
import com.piggybank.modele.Depense;
import com.piggybank.modele.Tirelire;

public class DepensesActivity extends AppCompatActivity
{
    private Controle controle;
    private Depense[] depenses;
    private Tirelire tirelire;

    private TextView depenses_textview_titre;
    private LinearLayout depenses_linearlayout_depense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depenses);
        init();
    }

    public void init()
    {
        this.controle = Controle.getInstance(this);

        Intent mIntent = getIntent();
        if(mIntent != null) {
            tirelire = mIntent.getParcelableExtra("tirelire");
        }
        depenses = controle.getDepenses(tirelire.getIdTirelire());

        depenses_textview_titre  = findViewById(R.id.depenses_textview_titre);
        depenses_linearlayout_depense = findViewById(R.id.depenses_linearlayout_depense);

        //AFFICHAGE DES DEPENSES
        final LinearLayout[] linearlayout_depense = new LinearLayout[depenses.length];
        final TextView[] textview_depense = new TextView[depenses.length];
        final Button[] btn_edit_depense = new Button[depenses.length];
        final Button[] btn_delete_depense = new Button[depenses.length];

        for(int i=0; i<depenses.length; i++)
        {
            linearlayout_depense[i] = new LinearLayout(DepensesActivity.this);
            linearlayout_depense[i].setBackgroundResource(R.color.white);
            linearlayout_depense[i].setPadding(20,20,20,20);
            linearlayout_depense[i].setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 15, 0, 15);
            linearlayout_depense[i].setLayoutParams(params);

            //La depense
            textview_depense[i] = new TextView(DepensesActivity.this);
            String depense = depenses[i].getTitre() + ": " + depenses[i].getMontant();
            textview_depense[i].setText(depense);
            textview_depense[i].setTextSize(25);
            textview_depense[i].setTextColor(Color.BLACK);
            linearlayout_depense[i].addView(textview_depense[i]);

            //le bouton edit
            btn_edit_depense[i] = new Button(DepensesActivity.this);
            btn_edit_depense[i].setText("Edit");
            btn_edit_depense[i].setTextColor(Color.parseColor("#4B4B71"));
            linearlayout_depense[i].addView(btn_edit_depense[i]);

            //le bouton delete
            btn_delete_depense[i] = new Button(DepensesActivity.this);
            btn_delete_depense[i].setText("Delete");
            btn_delete_depense[i].setTextColor(Color.parseColor("#4B4B71"));
            linearlayout_depense[i].addView(btn_delete_depense[i]);

            depenses_linearlayout_depense.addView(linearlayout_depense[i]);

            ecouterBtnEditDepense(btn_edit_depense[i], depenses[i]);
            ecouterBtnDeleteDepense(btn_delete_depense[i], depenses[i].getIdDepense());
        }
    }

    public void ecouterBtnDeleteDepense(Button btn_delete_depense, int id_depense)
    {
        btn_delete_depense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                controle.deleteDepense(id_depense);
                Toast.makeText(DepensesActivity.this, R.string.toast_depense_deleted, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DepensesActivity.this, TirelireActivity.class);
                startActivity(intent);
            }
        });
    }

    public void ecouterBtnEditDepense(Button btn_edit_depense, Depense depense)
    {
        btn_edit_depense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Ouverture du pop up
                AlertDialog.Builder dialogEditDepense = new AlertDialog.Builder(DepensesActivity.this);
                dialogEditDepense.setTitle(R.string.edit_expense);

                //création d'un linear layout verticale
                LinearLayout layoutEditDepense = new LinearLayout(DepensesActivity.this);
                layoutEditDepense.setOrientation(LinearLayout.VERTICAL);

                //Le titre de la dépense
                final TextView titre = new TextView(DepensesActivity.this);
                titre.setText("Titre:");
                layoutEditDepense.addView(titre);
                final EditText titreDepense = new EditText(DepensesActivity.this);
                titreDepense.setText(depense.getTitre());
                titreDepense.setInputType(InputType.TYPE_CLASS_TEXT);
                layoutEditDepense.addView(titreDepense);

                //Le montant de la dépense
                final TextView montant = new TextView(DepensesActivity.this);
                montant.setText("Montant (en euros):");
                layoutEditDepense.addView(montant);
                final EditText montantDepense = new EditText(DepensesActivity.this);
                montantDepense.setText(String.valueOf(depense.getMontant()));
                montantDepense.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                layoutEditDepense.addView(montantDepense);

                //Le commentaire de la dépense
                final TextView commentaire = new TextView(DepensesActivity.this);
                commentaire.setText("Commentaire:");
                layoutEditDepense.addView(commentaire);
                final EditText commentaireDepense = new EditText(DepensesActivity.this);
                commentaireDepense.setText(depense.getCommentaire());
                commentaireDepense.setInputType(InputType.TYPE_CLASS_TEXT);
                layoutEditDepense.addView(commentaireDepense);

                //Le jour de la dépense
                final TextView jour = new TextView(DepensesActivity.this);
                jour.setText("Date de la dépense:");
                layoutEditDepense.addView(jour);
                final EditText jourDepense = new EditText(DepensesActivity.this);
                jourDepense.setText(String.valueOf(depense.getJour()));
                jourDepense.setInputType(InputType.TYPE_CLASS_NUMBER);
                layoutEditDepense.addView(jourDepense);


                dialogEditDepense.setView(layoutEditDepense);

                //Bouton OK
                dialogEditDepense.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        //modification de la dépense
                        controle.setDepense(depense, titreDepense.getText().toString(), Double.parseDouble(montantDepense.getText().toString()), commentaireDepense.getText().toString(), Integer.parseInt(jourDepense.getText().toString()));
                        Toast.makeText(DepensesActivity.this, R.string.toast_depense_modifié, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DepensesActivity.this, TirelireActivity.class);
                        startActivity(intent);
                    }
                });

                //BOUTON CANCEL
                dialogEditDepense.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                });
                dialogEditDepense.show();
            }
        });
    }
}