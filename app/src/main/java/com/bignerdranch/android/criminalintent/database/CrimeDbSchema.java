package com.bignerdranch.android.criminalintent.database;

/**
 * Created by joseluiscastillo on 9/26/15.
 */
public class CrimeDbSchema {


    /* Inner class to describe the table.
    *  This class exists only to define the String constants needed to describe the moving pieces of
    *  the table definition. */
    public static final class CrimeTable {   // Name of the table

        public static final String NAME = "crimes";

        public static final class Cols {  // Defining the columns
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}
