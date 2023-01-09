package com.piggybank.outils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.piggybank.modele.Depense;
import com.piggybank.modele.Tirelire;

public class AccesLocal
{
    //propriétés
    private String nomBase = "dbPiggyBank.sqlite";
    private Integer versionBase = 1;
    private MySQLiteOpenHelper accesBD;
    private SQLiteDatabase bd;

    /**
     * Constructeur
     * @param context de la page
     */
    public AccesLocal(Context context)
    {
        accesBD = new MySQLiteOpenHelper(context, nomBase, null, versionBase);
    }

    /**
     * ajout d'une tirelire dans la bdd
     * @param tirelire à ajouter
     */
    public void ajoutTirelire(Tirelire tirelire)
    {
        bd = accesBD.getWritableDatabase();
        String req = "insert into tirelire (id, nom, contenu) values ";
        req+= "("+tirelire.getIdTirelire()+",\""+tirelire.getNom()+"\","+tirelire.getContenu()+")";
        bd.execSQL(req);
    }

    public void ajoutDepense(Depense depense)
    {
        bd = accesBD.getWritableDatabase();
        String req = "insert into depense (id, titre, montant, commentaire, jour, is_negative, id_tirelire) values ";
        req+= "("+depense.getIdDepense()+",\""+depense.getTitre()+"\","+depense.getMontant()+",\""+depense.getCommentaire()+"\",";
        req+= depense.getJour()+","+depense.getIs_negative()+","+depense.getIdTirelire()+")";
        bd.execSQL(req);
    }

    /**
     * suprrime une tirelire
     * @param idTirelire tirelire à supprimer
     */
    public void deleteTirelire(int idTirelire)
    {
        bd = accesBD.getWritableDatabase();
        bd.delete("tirelire", "id = ?", new String[]{String.valueOf(idTirelire)});
        bd.delete("depense", "id_tirelire = ?", new String[]{String.valueOf(idTirelire)});
        bd.close();
    }

    /**
     *
     * @return dernière tirelire créer
     */
    public Tirelire recupDernierTirelire()
    {
        bd = accesBD.getReadableDatabase();
        Tirelire tirelire = null;
        String req = "select * from tirelire";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToLast();
        if(!curseur.isAfterLast())
        {
            int id = curseur.getInt(0);
            String nom = curseur.getString(1);
            double contenu = curseur.getDouble(2);
            tirelire = new Tirelire(id, nom, contenu);
        }
        curseur.close();
        return tirelire;
    }
        /**
         *
         * @return dernière depense créer
         */
        public Depense recupDernierDepense()
        {
            bd = accesBD.getReadableDatabase();
            Depense depense = null;
            String req = "select * from depense";
            Cursor curseur = bd.rawQuery(req, null);
            curseur.moveToLast();
            if(!curseur.isAfterLast())
            {
                int id = curseur.getInt(0);
                String titre = curseur.getString(1);
                double montant = curseur.getDouble(2);
                String commentaire = curseur.getString(3);
                int jour = curseur.getInt(4);
                int is_negative = curseur.getInt(5);
                int id_tirelire = curseur.getInt(6);
                depense = new Depense(id, titre, montant, commentaire, jour, is_negative, id_tirelire);
            }
            curseur.close();
            return depense;
        }

    /**
     *
     * @return une tirelire de la bdd via sa position
     */
    public Tirelire getTirelire(int position)
    {
        bd = accesBD.getReadableDatabase();
        Tirelire tirelire;

        String req = "select * from tirelire";
        @SuppressLint("Recycle") Cursor curseur = bd.rawQuery(req, null);

        curseur.moveToFirst();
        for (int i=1; i<position; i++)
        {
            curseur.moveToNext();
        }

        int id = curseur.getInt(0);
        String nom = curseur.getString(1);
        double contenu = curseur.getDouble(2);
        tirelire = new Tirelire(id, nom, contenu);

        return tirelire;
    }

    /**
     *
     * @return un tableau d'id des tirelires
     */
    public String[] getIdTirelires()
    {
        bd = accesBD.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = bd.rawQuery("select id from tirelire", new String[]{});
        String[] idTirelires = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext())
        {
            idTirelires[i] = c.getString(0);
            i++;
        }
        return idTirelires;
    }

    /**
     *
     * @return un tableau avec tous les noms des tirelires
     */
    public String[] getNameTirelires()
    {
        bd = accesBD.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = bd.rawQuery("select nom from tirelire", new String[]{});
        String[] nameTirelires = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext())
        {
            nameTirelires[i] = c.getString(0);
            i++;
        }
        return nameTirelires;
    }

    /**
     * Modifier le contenu de la tirelire dans la bdd
     * @param tirelire tirelire a modifier
     * @param contenu nouveau contenu
     */
    public void setTirelire(Tirelire tirelire, String nom, double contenu)
    {
        bd = accesBD.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nom", nom);
        cv.put("contenu", contenu);
        int id = tirelire.getIdTirelire();
        bd.update("tirelire", cv, "id = ?", new String[]{String.valueOf(id)});
    }

    /**
     * récupère toutes les dépenses de la tirelire en fonction du jour
     * @param id_tirelire de la tirelire choisi
     * @param jour de la dépense
     * @return une liste de dépense du jour
     */
    public Depense[] getDepensesJour(int id_tirelire, int jour)
    {
        bd = accesBD.getWritableDatabase();
        Cursor c = bd.rawQuery("select * from depense where id_tirelire = ? and jour = ? ", new String[]{String.valueOf(id_tirelire), String.valueOf(jour)});
        Depense[] depenses = new Depense[c.getCount()];
        c.moveToFirst();
        for(int i=0; i<c.getCount(); i++)
        {
            int id = c.getInt(0);
            String titre = c.getString(1);
            double montant = c.getDouble(2);
            String commentaire = c.getString(3);
            jour = c.getInt(4);
            id_tirelire = c.getInt(5);
            int is_negatibve = c.getInt(6);

            depenses[i] = new Depense(id, titre, montant, commentaire, jour,is_negatibve, id_tirelire);
            c.moveToNext();
        }
        c.close();
        return depenses;
    }

    /**
     * Récupère toutes les dépenses de la tirelire
     * @param id_tirelire de la tirelire choisi
     * @return la liste des dépenses
     */
    public Depense[] getDepenses(int id_tirelire)
    {
        bd = accesBD.getWritableDatabase();
        Cursor c = bd.rawQuery("select * from depense where id_tirelire = ? ", new String[]{String.valueOf(id_tirelire)});
        Depense[] depenses = new Depense[c.getCount()];
        c.moveToFirst();
        for(int i=0; i<c.getCount(); i++)
        {
            int id = c.getInt(0);
            String titre = c.getString(1);
            double montant = c.getDouble(2);
            String commentaire = c.getString(3);
            int jour = c.getInt(4);
            id_tirelire = c.getInt(5);
            int is_negatibve = c.getInt(6);

            depenses[i] = new Depense(id, titre, montant, commentaire, jour,is_negatibve, id_tirelire);
            c.moveToNext();
        }
        c.close();
        return depenses;
    }

    /**
     * pour supprimer une dépense de la BDD
     * @param idDepense de la dépense à supprimer
     */
    public void deleteDepense(int idDepense)
    {
        bd = accesBD.getWritableDatabase();
        bd.delete("depense", "id = ?", new String[]{String.valueOf(idDepense)});
        bd.close();
    }

    public void setDepense(Depense depense, String titre, double montant, String commentaire, int jour)
    {
        bd = accesBD.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("titre", titre);
        cv.put("montant", montant);
        cv.put("commentaire", commentaire);
        cv.put("jour", jour);
        int id = depense.getIdDepense();
        bd.update("depense", cv, "id = ?", new String[]{String.valueOf(id)});
    }
}
