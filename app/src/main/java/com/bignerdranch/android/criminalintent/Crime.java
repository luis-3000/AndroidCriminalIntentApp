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

    //Constructor
    public Crime() {
        //Generate unique identifier for each "office crime"
        mId = UUID.randomUUID();
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
}
