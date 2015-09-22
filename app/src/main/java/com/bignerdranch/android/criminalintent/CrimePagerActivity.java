package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.UUID;

/**
 * Created by joseluiscastillo on 9/22/15.
 */
public class CrimePagerActivity extends FragmentActivity {

    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    /* */
    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        //Set up the ViewPager's pager adapter
        //Find the ViewPager in the activity's view
        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        //Get data from CrimeLab, the list of crimes
        mCrimes = CrimeLab.get(this).getCrimes();

        //Get the activity's instance of Fragmentmanager
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Set the adapter to be an unnamed instance of FragmentStatePagerAdapter which in turn requires the
        //FragmentManager.
        //FragmentStatePagerAdapter is the agent managing the converstaion with ViewPager
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            // For the agent to do its job with the fragments that getItem(int) returns, it needs to be able
            // to add them to the activity, tha tis why it need the Fragmentmanger.
            // (What the agent is actually doing is that it is adding the fragments I return to the activity
            // and helping ViewPager identify the fragment's views so that they can be placed correctly.)
            /* This method fetches the Crime instance for the given position in the dataset. It then uses
            * that crime's ID to create and return a properly configured CrimeFragment. */
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);

                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            /* Returns the number of items in the array list.*/
            public int getCount() {
                return mCrimes.size();
            }
        });

        //Loop though and find the index of the crime selected on screen so that when we hit the 'back'
        //button it displays this same item instead of re-setting to showing the first item in the list
        for(int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
