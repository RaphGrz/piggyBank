package com.piggybank.controleur;

import android.content.Context;

import com.piggybank.modele.Depense;
import com.piggybank.modele.Tirelire;
import com.piggybank.outils.AccesLocal;
import com.piggybank.outils.AdapterTirelire;

public class Controle
{
    private static Controle instance = null;
    private static Tirelire tirelire;
    private static Depense depense;
    private static AccesLocal accesLocal;
    AdapterTirelire mAdapterTirelire;

    /**
     * constructeur private
     */
    public Controle()
    {
        super();
    }

    /**
     * creation de l'instance
     * @return instance
     */
    public static Controle getInstance(Context context)
    {
        if(Controle.instance == null)
        {
            Controle.instance = new Controle();
            accesLocal = new AccesLocal(context);
        }
        return Controle.instance;
    }

    /**
     * creation de la tirelire + ajout à la bd
     * @param nom de tirelire
     */
    public void creerTirelire(String nom)
    {
        int id;
        if(accesLocal.recupDernierTirelire() != null)
        {
            id = accesLocal.recupDernierTirelire().getIdTirelire();
            id++;
        } else
            id = 1;
        tirelire = new Tirelire(id, nom);
        accesLocal.ajoutTirelire(tirelire);
    }

    public void creerDepense(String titre, double montant, String commentaire, int jour, int is_negative, int id_tirelire)
    {
        int id;
        if(accesLocal.recupDernierDepense() != null)
        {
            id = accesLocal.recupDernierDepense().getIdDepense();
            id++;
        } else
            id = 1;
        depense = new Depense(id, titre, montant, commentaire, jour, is_negative, id_tirelire);
        accesLocal.ajoutDepense(depense);
    }

    public void deleteTirelire(int idTirelire)
    {
        accesLocal.deleteTirelire(idTirelire);
    }

    public Tirelire getTirelire(int id)
    {
        tirelire = accesLocal.getTirelire(id);
        return tirelire;
    }

    /**
     *
     * @return la liste des id des tirelires
     */
    public String[] getIdTirelires()
    {
        return accesLocal.getIdTirelires();
    }

    /**
     *
     * @return la liste des noms des tirelires
     */
    public String[] getNameTirelires()
    {

        return accesLocal.getNameTirelires();
    }

    /**
     *
     * @return adapterTirelire
     */
    public AdapterTirelire getAdapterTirelire(Context context)
    {
        String[] nameTirelires = accesLocal.getNameTirelires();

        mAdapterTirelire = new AdapterTirelire(context, nameTirelires);

        return mAdapterTirelire;
    }

    /**
     *
     * @return la dernière tirelire créer
     */
    public Tirelire getDerniereTirelire()
    {
        return accesLocal.recupDernierTirelire();
    }

    public void setTirelire(Tirelire tirelire, String nom, double contenu)
    {
        accesLocal.setTirelire(tirelire, nom, contenu);
    }

    /**
     * récupère une liste de dépenses de la tirelire en fonction du jour
     * @param id_tirelire de la tirelire choisi
     * @param jour de la dépense
     * @return une liste de dépense de la tirelire en fonction du jour
     */
    public Depense[] getDepensesJour(int id_tirelire, int jour)
    {
        return accesLocal.getDepensesJour(id_tirelire, jour);
    }

    /**
     * récupère une liste de dépense via l'id d'une tirelire
     * @param id_tirelire de la tirelire
     * @return une liste de dépense de la tirelire
     */
    public Depense[] getDepenses(int id_tirelire)
    {
        return accesLocal.getDepenses(id_tirelire);
    }

    public void deleteDepense(int id_depense)
    {
        accesLocal.deleteDepense(id_depense);
    }

    public void setDepense(Depense depense, String titre, double montant, String commentaire, int jour)
    {
        accesLocal.setDepense(depense, titre, montant, commentaire, jour);
    }

}
