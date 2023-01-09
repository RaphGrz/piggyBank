package com.piggybank.outils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper
{
    //propriétés
    private final String creationTableTirelire = "create table tirelire(id INTEGER PRIMARY KEY, nom TEXT NOT NULL, contenu REAL)";
    private final String creationTableDepense = "create table depense(id INTEGER PRIMARY KEY, titre TEXT NOT NULL, montant REAL" +
            ", commentaire TEXT, jour INTEGER, id_tirelire INTEGER, is_negative INTEGER, FOREIGN KEY(id_tirelire) REFERENCES tirelire(id))";

    /**
     * constructeur
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * si changement de base de donnée
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(creationTableTirelire);
        sqLiteDatabase.execSQL(creationTableDepense);
    }

    /**
     * Si changement de version
     * @param sqLiteDatabase
     * @param i ancienne version
     * @param i1 nouvelle version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
