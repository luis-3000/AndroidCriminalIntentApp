package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by joseluiscastillo on 9/23/15.
 */
public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Inflate the dialog_date view and set it on the dialog further down
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

                //Build an AlertDialog with a title and one OK button
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)   //accepts a string resource and an object that implements the DialogInterface
                .create(); //returns the configured AlertDialog instance
    }
}
