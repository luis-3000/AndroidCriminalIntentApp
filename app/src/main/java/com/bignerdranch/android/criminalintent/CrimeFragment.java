package com.bignerdranch.android.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by joseluiscastillo on 9/16/15.
 */
public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    @Override
    /* Must be public because it will be called by any activity(ies)
    *  hosting the fragment. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    }

    @Override
    /* Inflates the view of fragment_crime.xml  */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
                                                          //View's parent /->false, whether to add the inflated view to the view's parent
                                                                        // The view will be added in the activity's code
        mTitleField = (EditText) v.findViewById(R.id.crime_title);

        /* Anonymous class that implements the TextWatcher listener interface. */
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString()); //Set crime's title to user's input
            }

            @Override
            public void afterTextChanged(Editable s) {
                //space intentionally left blank
            }
        });

        //Get a refence to the date button
        mDateButton = (Button)v.findViewById(R.id.crime_date);

        //Set it's text as the date of the crime
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false); //Temporarily disable the Date button

        //Get a reference for the CheckBox
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);


        //Set a listener that will update the mSolved field of the crime
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Set the crime's solved property
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }



}
