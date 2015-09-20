package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joseluiscastillo on 9/19/15.
 */
public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;

    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Hook up the view to the fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);

        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    /* Havind the CrimeAdapter, now it's time to connect it to the RecyclerView.
    * This method sets up CrimeListFragment's user interface. Initial functionality will be to
    * create a CrimeAdapter and set it on the RecyclerView. Other functionality to be added later. */
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        mAdapter = new CrimeAdapter(crimes);

        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    /* Defining the ViewHolder asn an inner class. */
    private class CrimeHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;

        public CrimeHolder( View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView;
        }
    }

    /* Another private inner class to define the CrimeAdapter.
    * This adapter will know all of Crime's details. */
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        /* Called by the RecyclerView when it needs a new Viwe to display an item.
        * In it, a View* is created and then it is wrapped in a ViewHolder. */
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            /* For a 'View' a layout is inflated from the Android standard library called
            * simple_list_item_1 which contains a single TextView, styled to look nice in a list.*/
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        /* Binds a ViewHolder's View to the model object. It reeived the ViewHolder and a position
        * in the data set. To bind the 'View' the position is used to find the right model data.
        * Then, the View is updated to reflect that model data. For this implementation, that position
        * is the index of the Crime in the array of crimes. Once it is pulled out, that indicated
        * 'Crime' object is bound to the 'View' by sending its title to the ViewHolder's TextView.*/
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.mTitleTextView.setText(crime.getTitle());
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}
