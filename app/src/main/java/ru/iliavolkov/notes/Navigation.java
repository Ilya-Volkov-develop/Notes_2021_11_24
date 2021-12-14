package ru.iliavolkov.notes;

import android.content.res.Resources;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.iliavolkov.notes.data.NotesData;
import ru.iliavolkov.notes.ui.ShowFragNote;

public class Navigation {

    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setFragment(Fragment fragment, int id,boolean useBackStack, Resources resources) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (!Utils.isLandscape(resources)) transaction.addToBackStack("");
        if (useBackStack) transaction.addToBackStack("");
        transaction.commit();
    }
//    public void addFragment(Fragment fragment, boolean useBackStack) {
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//
//        transaction.commit();
//    }
}
