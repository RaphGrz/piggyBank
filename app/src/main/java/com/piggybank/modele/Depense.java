package com.piggybank.modele;

import android.os.Parcel;
import android.os.Parcelable;

public class Depense implements Parcelable
{
    private int idDepense;
    private String titre;
    private double montant;
    private String commentaire;
    private int jour;
    private int is_negative;
    private int idTirelire;


    public Depense(int idDepense, String titre, double montant, String commentaire, int jour, int is_negative, int idTirelire)
    {
        this.idDepense = idDepense;
        this.titre = titre;
        this.montant = montant;
        this.commentaire = commentaire;
        this.jour = jour;
        this.is_negative = is_negative;
        this.idTirelire = idTirelire;
    }

    public int getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(int idDepense) {
        this.idDepense = idDepense;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getJour() {
        return jour;
    }

    public void setJour(int jour) {
        this.jour = jour;
    }

    public int getIdTirelire() {
        return idTirelire;
    }

    public void setIdTirelire(int idTirelire) {
        this.idTirelire = idTirelire;
    }

    public int getIs_negative() {
        return is_negative;
    }

    public void setIs_negative(int is_negative) {
        this.is_negative = is_negative;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
