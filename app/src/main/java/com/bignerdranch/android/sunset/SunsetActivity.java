package com.bignerdranch.android.sunset;

import android.support.v4.app.Fragment;

/**
 * Created by bofcarbon1 on 8/27/2017.
 */

public class SunsetActivity extends SingleFragmentActivity {

     @Override
    public Fragment createFragment() {

         return SunsetFragment.newInstance();
    }

}