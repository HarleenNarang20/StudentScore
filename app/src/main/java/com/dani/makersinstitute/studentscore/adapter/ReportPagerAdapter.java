package com.dani.makersinstitute.studentscore.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dani.makersinstitute.studentscore.views.home.ProfileFragment;
import com.dani.makersinstitute.studentscore.views.home.ReportFragment;

/**
 * Created by dani@taufani.com on 1/25/17.
 */
public class ReportPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Profile", "My Report" };
    private Context context;

    public ReportPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 1:
                ReportFragment reportFragment = new ReportFragment();
                return reportFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
