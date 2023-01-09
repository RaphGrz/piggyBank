package com.piggybank.vue;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.piggybank.R;
import com.piggybank.controleur.Controle;
import com.piggybank.modele.Depense;
import com.piggybank.modele.Tirelire;

public class TirelireActivity extends AppCompatActivity
{

    //Propriétés
        //private Controle controle;
    private int idTirelire;
    private String nomTirelire;
    private double contenuTirelire;
    private double contenuApresDepense;
    private Tirelire mTirelire;
    private Controle controle;
    private int is_negative;


    private TextView tirelire_textview_titre;
    private TextView tirelire_textview_contenu;
    private ImageButton tirelire_imageview_editmontant;
    private LinearLayout tirelire_linearlayout_depense;
    private TextView tirelire_textview_apresdepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tirelire);
        init();
    }

    @SuppressLint("SetTextI18n")
    public void init()
    {
        this.controle = Controle.getInstance(this);

        Intent mIntent = getIntent();
        if(mIntent != null) {
            mTirelire = mIntent.getParcelableExtra("tirelire");
        }
        idTirelire = mTirelire.getIdTirelire();
        nomTirelire = mTirelire.getNom();
        contenuTirelire = mTirelire.getContenu();
        contenuApresDepense = mTirelire.getContenu();

        tirelire_textview_titre = findViewById(R.id.tirelire_textview_titre);
        tirelire_textview_apresdepense = findViewById(R.id.tirelire_textview_apresdepense);
        tirelire_textview_titre.setText(nomTirelire);

        tirelire_textview_contenu = findViewById(R.id.tirelire_textview_contenu);
        tirelire_textview_contenu.setText(contenuTirelire +" €");

        tirelire_imageview_editmontant = findViewById(R.id.tirelire_imageview_editmontant);

        ecouterBtnEditMontant();

        //AFFICHAGE DES DEPENSES EN FONCTION DE LEUR DATE
        tirelire_linearlayout_depense = findViewById(R.id.tirelire_linearlayout_depense);
        final LinearLayout[] linearlayout_jour = new LinearLayout[31];
        final TextView[] textview_jour = new TextView[31];
        final TextView[] textview_contenuApresDepense = new TextView[31];
        final TextView[][] textview_depense = new TextView[31][];

        for(int i = 0; i<31; i++)
        {
            int jour = i+1;
            linearlayout_jour[i] = new LinearLayout(TirelireActivity.this);
            linearlayout_jour[i].setBackgroundResource(R.color.white);
            linearlayout_jour[i].setPadding(20,20,20,20);
            linearlayout_jour[i].setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 15, 0, 15);
            linearlayout_jour[i].setLayoutParams(params);

            textview_jour[i] = new TextView(TirelireActivity.this);
            textview_jour[i].setText("Jour " + (jour) + ":");
            textview_jour[i].setTextSize(25);
            textview_jour[i].setTextColor(Color.BLACK);

            linearlayout_jour[i].addView(textview_jour[i]);

            Depense[] depenses = controle.getDepensesJour(idTirelire, jour);
            if(depenses != null)
            {
                for(int d=0; d<depenses.length; d++)
                {
                    textview_depense[i] = new TextView[depenses.length];
                    textview_depense[i][d] = new TextView(TirelireActivity.this);
                    String depense = depenses[d].getTitre() + ": " + depenses[d].getMontant() + " € -> " + depenses[d].getCommentaire();
                    textview_depense[i][d].setText(depense);
                    textview_depense[i][d].setTextSize(20);
                    if(depenses[d].getIs_negative() != 0)
                    {
                        textview_depense[i][d].setTextColor(Color.parseColor("#308276"));
                        contenuApresDepense = contenuApresDepense + depenses[d].getMontant();
                    }
                    else
                    {
                        textview_depense[i][d].setTextColor(Color.parseColor("#ae0014"));
                        contenuApresDepense = contenuApresDepense - depenses[d].getMontant();
                    }
                    linearlayout_jour[i].addView(textview_depense[i][d]);
                }
            }
            textview_contenuApresDepense[i] = new TextView(TirelireActivity.this);
            textview_contenuApresDepense[i].setText("Fin de journée: "+contenuApresDepense +" €");
            linearlayout_jour[i].addView(textview_contenuApresDepense[i]);

            tirelire_linearlayout_depense.addView(linearlayout_jour[i]);
        }
        tirelire_textview_apresdepense.setText("Fin du mois: " + contenuApresDepense + " €");
    }

    /**
     *
     * @param menu menu
     * @return le menu de tirelire_menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.tirelire_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * écoute les items du menu
     * @param item les items
     * @return les items
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_add_depense:
                actionBtnAddDepense();
                return true;
            case R.id.menu_item_edit_name:
                actionBtnEditNomTirelire();
                return true;
            case R.id.menu_item_show_expense:
                Intent intentDepenses = new Intent(TirelireActivity.this, DepensesActivity.class);
                intentDepenses.putExtra("tirelire", mTirelire); // la clé et la tirelire
                startActivity(intentDepenses);
                return true;
            case R.id.menu_item_delete:
                AlertDialog.Builder dialogEditNom = new AlertDialog.Builder(TirelireActivity.this);
                dialogEditNom.setTitle(R.string.title_alerte_delete_tirelire);
                //Bouton OK
                dialogEditNom.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        controle.deleteTirelire(idTirelire);
                        Toast.makeText(TirelireActivity.this, R.string.toast_piggybank_deleted, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TirelireActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                //BOUTON CANCEL
                dialogEditNom.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                });
                dialogEditNom.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Ouvre pop up pour modifier le nom de la tirelire
     */
    public void actionBtnEditNomTirelire()
    {
        //Ouverture du pop up
        AlertDialog.Builder dialogEditNom = new AlertDialog.Builder(TirelireActivity.this);
        dialogEditNom.setTitle(R.string.title_alerte_edit_nom);

        //recupération du montant entré
        final EditText nomInput = new EditText(TirelireActivity.this);
        nomInput.setText(nomTirelire);
        nomInput.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogEditNom.setView(nomInput);

        //Bouton OK
        dialogEditNom.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //modification du montant
                mTirelire.setNom(nomInput.getText().toString());
                tirelire_textview_titre.setText(nomInput.getText().toString());
                controle.setTirelire(mTirelire, nomInput.getText().toString(), mTirelire.getContenu());
                Toast.makeText(TirelireActivity.this, R.string.toast_nom_modifié, Toast.LENGTH_LONG).show();
            }
        });

        //BOUTON CANCEL
        dialogEditNom.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        dialogEditNom.show();
    }

    /**
     * Ecoute le bouton pour modifier le montant
     */
    public void ecouterBtnEditMontant()
    {
        tirelire_imageview_editmontant.setOnClickListener(new ImageButton.OnClickListener()
        {
            /**
             * creation de la fenetre pop up pour modifier le contenu de la tirelire
             * @param v view
             */
            public void onClick(View v)
            {
                //Ouverture du pop up
                AlertDialog.Builder dialogEditMontant = new AlertDialog.Builder(TirelireActivity.this);
                dialogEditMontant.setTitle(R.string.title_alerte_edit_montant);

                //recupération du montant entré
                final EditText montantInput = new EditText(TirelireActivity.this);
                montantInput.setText(String.valueOf(contenuTirelire));
                montantInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                dialogEditMontant.setView(montantInput);

                //Bouton OK
                dialogEditMontant.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        //modification du montant
                        mTirelire.setContenu(Double.parseDouble(montantInput.getText().toString()));
                        tirelire_textview_contenu.setText(montantInput.getText().toString() +" €");
                        controle.setTirelire(mTirelire,mTirelire.getNom(), mTirelire.getContenu());
                        Toast.makeText(TirelireActivity.this, R.string.toast_montant_modifié, Toast.LENGTH_LONG).show();
                    }
                });

                //BOUTON CANCEL
                dialogEditMontant.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                });
                dialogEditMontant.show();
            }
        });
    }

    public void actionBtnAddDepense()
    {
        is_negative = 0;

        //Ouverture du pop up
        AlertDialog.Builder dialogAddDepense = new AlertDialog.Builder(TirelireActivity.this);
        dialogAddDepense.setTitle(R.string.add_expense);

        //création d'un linear layout verticale
        LinearLayout layoutAddDepense = new LinearLayout(TirelireActivity.this);
        layoutAddDepense.setOrientation(LinearLayout.VERTICAL);

        //Le titre de la dépense
        final TextView titre = new TextView(TirelireActivity.this);
        titre.setText("Titre:");
        layoutAddDepense.addView(titre);
        final EditText titreDepense = new EditText(TirelireActivity.this);
        titreDepense.setHint(R.string.titre);
        titreDepense.setInputType(InputType.TYPE_CLASS_TEXT);
        layoutAddDepense.addView(titreDepense);

        //Le montant de la dépense
        final TextView montant = new TextView(TirelireActivity.this);
        montant.setText("Montant (en euros):");
        layoutAddDepense.addView(montant);
        final EditText montantDepense = new EditText(TirelireActivity.this);
        montantDepense.setHint("0");
        montantDepense.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layoutAddDepense.addView(montantDepense);

        //Le commentaire de la dépense
        final TextView commentaire = new TextView(TirelireActivity.this);
        commentaire.setText("Commentaire:");
        layoutAddDepense.addView(commentaire);
        final EditText commentaireDepense = new EditText(TirelireActivity.this);
        commentaireDepense.setHint(R.string.commentaire);
        commentaireDepense.setInputType(InputType.TYPE_CLASS_TEXT);
        layoutAddDepense.addView(commentaireDepense);

        //Le jour de la dépense
        final TextView jour = new TextView(TirelireActivity.this);
        jour.setText("Date de la dépense:");
        layoutAddDepense.addView(jour);
        final EditText jourDepense = new EditText(TirelireActivity.this);
        jourDepense.setHint("Day 1 - 31");
        jourDepense.setInputType(InputType.TYPE_CLASS_NUMBER);
        layoutAddDepense.addView(jourDepense);

        //revenu ou dépense
        final TextView textViewRevenu = new TextView(TirelireActivity.this);
        textViewRevenu.setText("Est-ce un revenu ou une dépense?");
        layoutAddDepense.addView(textViewRevenu);
        final CheckBox checkBoxRevenu = new CheckBox(TirelireActivity.this);
        checkBoxRevenu.setText("Revenu");
        layoutAddDepense.addView(checkBoxRevenu);
        final CheckBox checkBoxDepense = new CheckBox(TirelireActivity.this);
        checkBoxDepense.setText("Dépense");
        layoutAddDepense.addView(checkBoxDepense);

        dialogAddDepense.setView(layoutAddDepense);

        //Bouton OK
        dialogAddDepense.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(checkBoxRevenu.isChecked())
                {
                    is_negative = 1;
                }
                //création de la dépense
                controle.creerDepense(titreDepense.getText().toString(), Double.parseDouble(montantDepense.getText().toString()), commentaireDepense.getText().toString(), Integer.parseInt(jourDepense.getText().toString()), is_negative, mTirelire.getIdTirelire());
                Toast.makeText(TirelireActivity.this, R.string.toast_depense_cree, Toast.LENGTH_LONG).show();
            }
        });

        //BOUTON CANCEL
        dialogAddDepense.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        dialogAddDepense.show();
    }
}