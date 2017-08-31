package com.bignerdranch.android.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import static com.bignerdranch.android.sunset.R.color.sea;

/**
 * Created by bofcarbon1 on 8/27/2017.
 */

public class SunsetFragment extends Fragment {

    private static final String TAG = "SunFragment";
    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;
    //Ch 30 Challenge - Reverse sunset
    private boolean mIsSunDown;
    private View mSeaView;
    private int mSeaColor;
    private int mSeaGradientColor;

    public static SunsetFragment newInstance() {

        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceBundle) {

        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        //ObjectAnimator sunPulseAnimator = ObjectAnimator.

        //Set the sun state boolean to false
        mIsSunDown = false;

        //Setup views
        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);
        mSeaView = view.findViewById(R.id.sea);

        //Setup colors
        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);
        //Ch 30 Challenge sun reflection
        mSeaColor = resources.getColor(R.color.sea);
        mSeaGradientColor = R.drawable.sea_gradient;

        //Start the sun set/riee animation when the screen is touched
        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(mSunView,
                PropertyValuesHolder.ofFloat("scaleX", 1.025f),
                PropertyValuesHolder.ofFloat("scaleY", 1.025f));
        scaleDown.setDuration(310);
        scaleDown.setInterpolator(new FastOutSlowInInterpolator());

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();

        return view;
    }

    private void startAnimation() {

        //Move the sun up or down to/from the sea
        if(!mIsSunDown) {

            //Make the sun set

            float sunYStart = mSunView.getTop();
            float sunYEnd = (mSkyView.getHeight() + 30);
            //Log.i(TAG, "mSkyView.getHeight: " + mSkyView.getHeight());

            ObjectAnimator heightAnimator = ObjectAnimator
                    .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                    .setDuration(3000);

            //Control the acceleration of the sun set
            heightAnimator.setInterpolator(new AccelerateInterpolator());


            //Change the color of th esky to orange as the sun sets
            ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                    .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                    .setDuration(3000);

            //Control the sunset color change
            sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

            //Change the color of th esky to a night sky after the sun sets
            ObjectAnimator nightSkyAnimator = ObjectAnimator
                    .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                    .setDuration(1500);

            //Control the night sky color change
            nightSkyAnimator.setEvaluator(new ArgbEvaluator());

            //heightAnimator.start();
            //sunsetSkyAnimator.start();

            //Sun reflection in the sea (But no of course it doesn't work
            //ObjectAnimator seaSunReflectionAnimator = ObjectAnimator
            //        .ofObject(mSeaView,
            //                "backgroundColor",
            //                new ArgbEvaluator(),
            //                mSeaColor,
            //                mSeaGradientColor);
            //seaSunReflectionAnimator.setDuration(1500);
            //seaSunReflectionAnimator.start();

            //Transition the color changes
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet
                    .play(heightAnimator)
                    .with(sunsetSkyAnimator)
                    .before(nightSkyAnimator);
            animatorSet.start();

            mIsSunDown = true;

        }
        else {

            //Ch 30 Challenge - Make the sun rise

            float sunYRStart = mSceneView.getBottom();
            float sunYREnd = mSunView.getTop();

            ObjectAnimator heightAnimator = ObjectAnimator
                    .ofFloat(mSunView, "y", sunYRStart, sunYREnd)
                    .setDuration(3000);

            //Control the acceleration of the sun set
            heightAnimator.setInterpolator(new AccelerateInterpolator());


            //Change the color of the sky to orange as the sun rises
            ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                    .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                    .setDuration(3000);

            //Control the sunset color change
            sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

            //Change the color of the sky to a blue sky after the sun rises
            ObjectAnimator nightSkyAnimator = ObjectAnimator
                    .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                    .setDuration(1500);

            //Control the night sky color change
            nightSkyAnimator.setEvaluator(new ArgbEvaluator());

            //Transition the color changes
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet
                    .play(heightAnimator)
                    .with(sunsetSkyAnimator)
                    .before(nightSkyAnimator);
            animatorSet.start();

            mIsSunDown = false;
        }

    }

}
