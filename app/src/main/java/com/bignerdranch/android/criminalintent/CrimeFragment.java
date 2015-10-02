package com.bignerdranch.android.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import android.text.format.DateFormat;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by joseluiscastillo on 9/16/15.
 */
public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0; //Constant for the request code
    private static final int REQUEST_CONTACT = 1;  //Request code from contact's list to list as a 'suspect'
    private static final int REQUEST_PHOTO=2;

    private Crime mCrime;
    private File mPhototFile;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton; //Button to request a 'suspect' from the contact's list
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;

    /*
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }


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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
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

        // Stash away the location of the photo file for use in other parts
        mPhototFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }


    @Override
    /* Updates the CrimeLab's copy of 'Crime' instances. This effectively updates the crimes list. */
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
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
            updateCrime();
            updateDate();
        } else if(requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();

            //Specify which fields you want your query to return values for
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };


            /* Next, we create a query that asks for all the display names of the contacts in the returned data,
             * then query the contact's database and get a Cursor object to work with. */

            //Perform your query - the contactUri is like a "where clause here
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);


            try {
                //Double check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }

                //Pull out the first column of the first row of data -  that is the suspect's name.
                c.moveToFirst(); //Because the curors only contains one item, we move it to the first item
                String suspect = c.getString(0);  //... and get it as a string, this string will be the name of the suspect.
                mCrime.setSuspect(suspect);       //set the suspect
                updateCrime();
                mSuspectButton.setText(suspect);  //set the corresponding button
            } finally {
                c.close();
            }
        } else  if (requestCode == REQUEST_PHOTO) {
            updateCrime();
            updatePhotoView();
        }
    }

    private void updateCrime() {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    /* Creates four strings and then pieces them together and return a complete report. */
    private String getCrimeReport() {
        String solvedString = null;

        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();

        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    /* Here, we load the Bitmap into the ImageView. */
    private void updatePhotoView() {
        if (mPhototFile == null || !mPhototFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhototFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
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
                updateCrime();
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
                updateCrime();
            }
        });

        //Send a report
        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               Intent i = new Intent(Intent.ACTION_SEND); //Intent constructor that accepts a string which is a constant defining the action
               i.setType("text/plain");
               i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
               i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
               //Make sure to always display a chooser to send the report (email/twitter,etc)
               i = Intent.createChooser(i, getString(R.string.send_report));
               startActivity(i);
           }
        });

        //Get a reference to the 'request suspect name button'
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect);

        //... and set a listener to it
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        //Guard against no contact app to pick 'suspects'
        //PackageManager know about all the components installed on the Android device, including all of its activities.
        PackageManager packageManager = getActivity().getPackageManager();

        /* By calling resolveActivity(Intent, int) we are asking it to find an activity that matches the Intent we gave it.
         * The MATCH_DEFAULT_ONLY flag restrict this search to activities with the CATEGORY_DEFAULT flag, just like startActivity(Intent) does.
         * If the search is successful, it will return an instance of ResolveInfo telling us all about which activity it found. In contracts, if
         * it returns null, the game is up, no contacts app - so we disable the useless suspect button. */
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null)
        {
            mSuspectButton.setEnabled(false);
        }

        // Now, handle the image button and photo view for the 'suspect' which will be pulled from the pictures folder on the device
        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_camera);

        // Create n Intent to ask for a new picture to be taken into the location saved in mPhotoFile
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if there is a location at which to save the photo AND if there is a camera app that can take the photo
        boolean canTakePhoto = mPhototFile != null && captureImage.resolveActivity(packageManager) != null;

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhototFile); //Tell where to save the photo image on the filesystem by passing Uri
                                                 //pointing to where we want to save the file in MediaStore.EXTRA_OUTPUT
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView)v.findViewById(R.id.crime_photo);

        // Call method to update the photo view with the correct bitmap size
        updatePhotoView();

        return v;
    }



}
