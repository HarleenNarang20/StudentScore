package com.dani.makersinstitute.studentscore.views.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dani.makersinstitute.studentscore.views.editprofile.EditProfileActivity;
import com.dani.makersinstitute.studentscore.R;
import com.dani.makersinstitute.studentscore.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView mTextViewName;
    private TextView mTextViewMajor;
    private SharedPreferences mSharedPreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mTextViewName = (TextView) view.findViewById(R.id.textViewName);
        mTextViewMajor = (TextView) view.findViewById(R.id.textViewMajor);

        mSharedPreferences = getContext().
                getSharedPreferences(Constants.KEY_SHARED_PREF, getContext().MODE_PRIVATE);

        if (mSharedPreferences.contains(Constants.KEY_NAME)
                || mSharedPreferences.contains(Constants.KEY_MAJOR)) {
            String name = mSharedPreferences.getString(Constants.KEY_NAME, "");
            String major = mSharedPreferences.getString(Constants.KEY_MAJOR, "");

            mTextViewName.setText(name);
            mTextViewMajor.setText(major);
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_EDIT_PROFILE
                && resultCode != getActivity().RESULT_CANCELED) {
            String name = mSharedPreferences.getString(Constants.KEY_NAME, "");
            String major = mSharedPreferences.getString(Constants.KEY_MAJOR, "");

            mTextViewName.setText(name);
            mTextViewMajor.setText(major);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivityForResult(intent, Constants.REQUEST_EDIT_PROFILE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
