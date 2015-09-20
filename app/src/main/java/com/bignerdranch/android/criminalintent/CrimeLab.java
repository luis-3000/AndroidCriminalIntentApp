package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

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

        for(int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime # " + i);
            crime.setSolved(i % 2 == 0); // Every other crime
            mCrimes.add(crime);
        }
    }

    /* Return the list of crimes. */
    public List<Crime> getCrimes() {
        return mCrimes;
    }

    /* Returns a CrimeLab instance. */
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) { //if instance does not exist
            sCrimeLab = new CrimeLab(context); //call constructor to create it
        }
        return sCrimeLab;
    }


}
