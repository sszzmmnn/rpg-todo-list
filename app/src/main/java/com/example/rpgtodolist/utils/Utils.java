package com.example.rpgtodolist.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rpgtodolist.R;

public class Utils {
    public static void replace(Fragment fragment, String fragmentTag, FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        for (Fragment f : activity.getSupportFragmentManager().getFragments()) {
            if (f != null) {
                activity.getSupportFragmentManager().beginTransaction().remove(f).commit();
            }
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, fragment, fragmentTag);
        fragmentTransaction.commit();
    }
}
