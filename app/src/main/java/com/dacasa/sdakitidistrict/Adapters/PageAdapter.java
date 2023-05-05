package com.dacasa.sdakitidistrict.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dacasa.sdakitidistrict.Fragments.AuthorsFragment;
import com.dacasa.sdakitidistrict.Fragments.ChurchCalenderFragment;
import com.dacasa.sdakitidistrict.Fragments.ChurchLevelFragment;
import com.dacasa.sdakitidistrict.Fragments.LessonFragment;
import com.dacasa.sdakitidistrict.Fragments.MissionFragment;
import com.dacasa.sdakitidistrict.Fragments.NotesFragment;
import com.dacasa.sdakitidistrict.MainCalender;

public class PageAdapter extends FragmentPagerAdapter {
    private int tabsNumber;

    public PageAdapter(@NonNull FragmentManager fm, int behavior,int tabs) {
        super(fm, behavior);
        this.tabsNumber = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ChurchLevelFragment();
            case 1:
                return new NotesFragment();
            case 2:
                return new MainCalender();
            case 3:
                return new MissionFragment();
            case 4:
                return new LessonFragment();
                default: return null;
        }


    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
