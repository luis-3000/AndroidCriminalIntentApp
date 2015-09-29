package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by joseluiscastillo on 9/18/15.
 * This will be a singleton class which will have a private constructor and a get() method.
 * A singleton is a class that allows only one instance of itself to be created.
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab; //static variable
    //private List<Crime> mCrimes; //List of crimes <-- Now will use mDatabase for storage instead
    private Context mContext; // Context stored in a variable for later use
    private SQLiteDatabase mDatabase;

    //Private constructor, other classes won't be able to create a CrimeLab object
    private CrimeLab (Context context) {

        //Uses SQLiteOpenHelper to create the 'crime' database.
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

        //mCrimes = new ArrayList<>(); //construct an list of crimes

    }


    /* Adds a new crime to thelist of crimes when user presses the New Crime action item
     * on the screen's top tool bar.  */
    public void addCrime(Crime c) {
        //mCrimes.add(c);

        //Adding tows to the database
        ContentValues values = getContentValues(c);

        //First argument = table we ant to insert into, Last argument is the data we to to put in.
        //Second agument is anullColumnHack which allows insertion on an empty ContentValues, if
        // needed perhaps later on, although not used now.
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME, null, values);
    }


    /* Returns the list of crimes. */
    public List<Crime> getCrimes() {

        //Iterate over the list of crimes to add them to the list
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst(); //To pull data out of a cursor, it needs to be moved to the first element
            while (!cursor.isAfterLast()) { //while not past the end of the dataset in the table
                crimes.add(cursor.getCrime()); // Read the row's data
                cursor.moveToNext(); //Advance to the next row in the table
            }

        } finally {
            cursor.close(); //Make sure to close the 'cursor' to prevent nasty error logs and running out
        }                   // of open file handles which could crash the app.

        //return mCrimes;
        //return new ArrayList<>();
        return crimes;
    }


    /* Very similar to getCrimes(), except that it will only need to pull the fist item, if it is there.
    *  Now, each CrimeFragment displayed in CrimePagerActivity is showing the real 'Crime' */
    public Crime getCrime(UUID id) {

        //return null;

        CrimeCursorWrapper cursor = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                                                    new String[] { id.toString() } );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }

    }


    /* Writes and updates to databases are done with the help of a class called 'ContentValues,
       * which is a key-value store class, and it is specifically designed to store the kinds of data
       * SQLite can hold. For the 'keys', the column names are used.
       * This method takes care of shuttling a 'Crime' into a 'ContentValues' instnce. */
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);

        return values;
    }

    /* Method to call query() on CrimeTable. */
    //private Cursor queryCrimes(String whereClause, String[] whereArgs) {
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) { //Wrap cursor we get back from the query
        Cursor cursor = mDatabase.query(CrimeDbSchema.CrimeTable.NAME,
                                        null,             // Columns - null selects all columns
                                        whereClause,
                                        whereArgs,
                                        null,             // groupBy
                                        null,             // having
                                        null              // orderBy
        );

        //return cursor;
        return new CrimeCursorWrapper(cursor);

    }

    /* Method to update rows in the database. */
    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,                    // Table name
                          values,                                         // ContentValues to assign to each row
                            CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",  // 'where' clause (specify which rows get updated)
                                new String[] { uuidString });             // 'String' to specify values for the arguments in
    }                                                                     // the 'where' clause.(Done this way in case the
                                                                          // 'String' itself might contain SQL code, which may
                                                                          // change the meaning of the query or even alter the
                                                                          // database [A.K.A = SQL injection attack] ).

    /* Returns a CrimeLab instance. */
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) { //if instance does not exist
            sCrimeLab = new CrimeLab(context); //call constructor to create it
        }
        return sCrimeLab;
    }


}
