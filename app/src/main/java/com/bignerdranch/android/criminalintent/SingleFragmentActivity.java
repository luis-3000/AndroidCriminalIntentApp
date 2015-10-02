package com.bignerdranch.android.criminalintent;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by joseluiscastillo on 9/19/15.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {


    @LayoutRes  //Tells Android Studio that any implementation of this method should return a valid layout resource ID
    /* Returns the ID of the layout that the activity will inflate. */
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    protected abstract Fragment createFragment(); //Abstract method to instantiate the fragment.
                                                 //Subclasses of SingleFramentActivity will implement
                                                 // this method to return an instance of the fragment
                                                 // that the activity is hosting.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_fragment); <-- substituted for below line
        setContentView(getLayoutResId()); //Now, subclasses of SingleFrameActivity can choose to override
                                          //this method to return a layout other than activity_fragment.xml

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
