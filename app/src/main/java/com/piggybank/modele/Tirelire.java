package com.piggybank.modele;

import android.os.Parcel;
import android.os.Parcelable;

public class Tirelire implements Parcelable
{
    private int idTirelire;
    private String nom;
    private double contenu ;

    /**
     * constructeur tirelire
     * @param id de la tirelire
     * @param nom de la tirelire
     */
    public Tirelire(int id, String nom)
    {
        this.idTirelire = id;
        this.nom = nom;
        this.contenu = 0;
    }

    /**
     * constructeur tirelire
     * @param idTirelire id de la tirelire
     * @param nom de la tire
     * @param contenu dans la tirelire
     */
    public Tirelire(int idTirelire,String nom, double contenu)
    {
        this.idTirelire = idTirelire;
        this.nom = nom;
        this.contenu = contenu;
    }

    /**
     * constructeur via parcel
     * @param in
     */
    protected Tirelire(Parcel in) {
        idTirelire = in.readInt();
        nom = in.readString();
        contenu = in.readDouble();
    }

    public static final Creator<Tirelire> CREATOR = new Creator<Tirelire>()
    {
        /**
         * créer une tirelire depuis une Parcel
         * @param in
         * @return une tirelire
         */
        @Override
        public Tirelire createFromParcel(Parcel in) {
            return new Tirelire(in);
        }

        /**
         *
         * @param size
         * @return tableau de tirelire
         */
        @Override
        public Tirelire[] newArray(int size) {
            return new Tirelire[size];
        }
    };

    public int getIdTirelire() {
        return idTirelire;
    }

    public void setIdTirelire(int id){ this.idTirelire = id; }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getContenu() {
        return contenu;
    }

    public void setContenu(double contenu) {
        this.contenu = contenu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(idTirelire);
        parcel.writeString(nom);
        parcel.writeDouble(contenu);
    }
}
