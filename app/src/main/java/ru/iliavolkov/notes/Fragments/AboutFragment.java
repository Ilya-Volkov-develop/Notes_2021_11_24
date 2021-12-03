package ru.iliavolkov.notes.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ru.iliavolkov.notes.MainActivity;
import ru.iliavolkov.notes.R;

public class AboutFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_fragment, container, false);
    }

    @Override
    public void onStop() {
        MainActivity.activityStr = "list";
        super.onStop();
    }
}
