package com.bignerdranch.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by joseluiscastillo on 9/27/15.
 * A SQLiteOpenHelper is a class designed to get rid of the grunt work of opening a SQLiteDatabase
 * It is used inside CrimeLab to create the crime database.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeDbSchema.CrimeTable.NAME + "(" +
            " _id integer pirmary key autoincrement, " +
            CrimeDbSchema.CrimeTable.Cols.UUID + ", " +
            CrimeDbSchema.CrimeTable.Cols.TITLE + ", " +
            CrimeDbSchema.CrimeTable.Cols.DATE + ", " +
            CrimeDbSchema.CrimeTable.Cols.SOLVED +
            ")"
        );
    }

    @Override
    /* Upgrades to newer versions of the current project. */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
