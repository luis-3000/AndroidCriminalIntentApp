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

import java.util.Date;
import java.util.UUID;

/**
 * Created by joseluiscastillo on 9/16/15.
 */
public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0; //Constant for the request code

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    /* Constructor that accepts a UUID, creates an arguments bundle, creates a fragment instance
    *  and then attaches the arguments to the fragment. */
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    /* Must be public because it will be called by any activity(ies)
    *  hosting the fragment. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();

        //Retrieve the UUID from the fragment arguments
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);

        //After getting the ID, it is used to fetch the Crime from CrimeLab.
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    /* Overrinding this method to make it retrieve the 'extra', set the date on the Crime, and
    * refresh the text of the date button. */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    @Override
    /* Inflates the view of fragment_crime.xml  */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime, container, false);
                                                          //View's parent /->false, whether to add the inflated view to the view's parent
                                                                        // The view will be added in the activity's code
        mTitleField = (EditText) v.findViewById(R.id.crime_title);

        //Now that CrimeFragment fetches a Crime, its view can display that Crime's data.
        //Updating this line to display the Crime's title
        mTitleField.setText(mCrime.getTitle());

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
        mDateButton = (Button) v.findViewById(R.id.crime_date);

        //Set it's text as the date of the crime
        updateDate();

        //Activate the DateButton
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();

                //DatePickerFragment dialog = new DatePickerFragment(); //replaced by below line

                //DatePickerFragment needs to initialize the DatePicker using the information held in the 'Date'
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());

                //Set CrimeFragment as the target fragment for DatePickerFragment
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);

                dialog.show(manager, DIALOG_DATE);
            }
        });

        //Get a reference for the CheckBox
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);

        //Display the solved status
        mSolvedCheckBox.setChecked(mCrime.isSolved());


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
