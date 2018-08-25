package com.liviurau.bakers.ui.step;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.liviurau.bakers.data.model.Step;

import java.util.List;

import static com.liviurau.bakers.ui.step.StepActivity.SELECTED_STEP;

class StepFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<Step> stepList;

    public StepFragmentPagerAdapter(List<Step> steps, FragmentManager fm) {
        super(fm);
        this.stepList = steps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(SELECTED_STEP, stepList.get(position));
        StepFragment fragment = new StepFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < 10){
            return String.format("0%s", String.valueOf(position));
        } else {
            return String.valueOf(position);
        }
    }

    @Override
    public int getCount() {
        return stepList.size();
    }
}
