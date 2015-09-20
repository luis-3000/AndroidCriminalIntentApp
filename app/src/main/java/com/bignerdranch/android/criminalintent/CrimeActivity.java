package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

public class CrimeActivity extends SingleFragmentActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment);
//
//
//        //Add the FragmentManager
//        FragmentManager fm = getSupportFragmentManager(); //called because using the support library and the FragmentActivity class
//
//        //Give the fragment manager a fragment to manage
//        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
//
//        if(fragment == null) {
//            fragment = new CrimeFragment(); //Instantiate a CrimeFragment before adding it to the FragmentManager
//            fm.beginTransaction().add(R.id.fragment_container, fragment).commit(); //This code creates and commits a 'fragment transaction'
//                                                                                   //Fragment transactions are used to add, remove, attach, detach or replace fragments in the fragment list.
//        }
//
//    }

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }


}
