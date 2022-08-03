package com.example.pdforganiser;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class viewPageAdapter extends FragmentPagerAdapter {

    public viewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                AllFragment allFragment = new AllFragment();
                return allFragment;
            case 1:
                AlbumFragment albumFragment = new AlbumFragment();
                return albumFragment;
            default:
                return null;
        }
        //return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
