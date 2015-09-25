package com.bignerdranch.android.criminalintent;

import android.content.Context;

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
    private List<Crime> mCrimes; //List of crimes

    //Privae constructor, other classes won't be able to create a CrimeLab object
    private CrimeLab (Context context) {
        mCrimes = new ArrayList<>(); //construct an list of crimes
    }

    /* Return the list of crimes. */
    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for(Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    /* Returns a CrimeLab instance. */
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) { //if instance does not exist
            sCrimeLab = new CrimeLab(context); //call constructor to create it
        }
        return sCrimeLab;
    }

    /* Adds a new crime to thelist of crimes when user presses the New Crime action item
     * on the screen's top tool bar.  */
    public void addCrime(Crime c) {
        mCrimes.add(c);
    }

}
