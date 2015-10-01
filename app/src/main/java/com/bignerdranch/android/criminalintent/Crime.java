package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by joseluiscastillo on 9/16/15.
 */
public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate; //The date an "office crime" occurred. Sets date to current date.
    private boolean mSolved;
    private String mSuspect; //To hold the name of a 'suspect'

    //Constructor
    public Crime() {
        //Generate unique identifier for each "office crime"
        //mId = UUID.randomUUID();
        //mDate = new Date();

        //Using CrimeCursorWrapper to return a Crime
        this(UUID.randomUUID());
    }

    // Overloaded constructor
    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    /* To get a well-known file name.
    *  This method wont' know what folder the phot will be stored in. However,
    *  the filename will be unique, since it is abased on the Crime's ID. */
    public String getPhotoFilename() {
        return "IMG_ " + getId().toString() + ".jpg";
    }

}
