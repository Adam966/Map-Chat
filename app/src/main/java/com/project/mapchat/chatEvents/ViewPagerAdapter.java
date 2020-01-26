package com.project.mapchat.chatEvents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.project.mapchat.chatEvents.fragments.FragmentEvents;
import com.project.mapchat.chatEvents.fragments.FragmentUserEvents;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles = new String[]{"My Events", "All Events"};

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        //super(fm, behavior);
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       switch(position){
           case 0: {
               return new FragmentUserEvents();
           }
           case 1: {
                return new FragmentEvents();
           }
           default:
               throw new RuntimeException("Invalid tab position");
       }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        switch (position) {
            case 0:
                return tabTitles[0];
            case 1:
                return tabTitles[1];
            default:
                return null;
        }
    }

}
