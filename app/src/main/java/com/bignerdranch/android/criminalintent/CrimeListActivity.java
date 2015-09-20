package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by joseluiscastillo on 9/19/15.
 */
public class CrimeListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
