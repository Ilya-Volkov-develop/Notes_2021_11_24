package ru.iliavolkov.notes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static ru.iliavolkov.notes.NoteActivityShow.ARG_DATE;
import static ru.iliavolkov.notes.NoteActivityShow.ARG_DESCRIPTION;
import static ru.iliavolkov.notes.NoteActivityShow.ARG_INDEX;
import static ru.iliavolkov.notes.NoteActivityShow.ARG_TITLE;

public class NotesFragment extends Fragment {

    private static final String CURRENT_NOTES = "CurrentNotes";
    // Текущая позиция (выбранный город)
    private int currentPosition = 0;
    private TextView title;
    private TextView titleDate;
    private TextView description;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null){
            currentPosition = savedInstanceState.getInt(CURRENT_NOTES, 0);
        }

        initList(view);

        //if (Utils.isLandscape(getResources())) showLandCoatOfArms(currentPosition);

    }

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout_list = inflater.inflate(R.layout.note_list, null);
        title = layout_list.findViewById(R.id.titleList);
        titleDate = layout_list.findViewById(R.id.titleDataList);
        description = layout_list.findViewById(R.id.descriptionList);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString("arrNotes", null);
        Type type = new TypeToken<ArrayList<Notes>>() {}.getType();
        ArrayList<Notes> arrNotes = gson.fromJson(json, type);
        int position = 0;

        try {
            for (Notes notes: arrNotes) {
                position++;
                title.setText(notes.getTitle());
                titleDate.setText(notes.getDate());
                description.setText(notes.getDescription());
                int finalPosition = position;
                layout_list.setOnClickListener(v->{
                    currentPosition = finalPosition;
                    showNote(finalPosition,title.getText().toString(),description.getText().toString(),titleDate.getText().toString());
                });
                layoutView.addView(layout_list);
            }
        } catch (Exception e) {}
    }

    private void showNote(int position, String title, String description, String titleDate) {
        if (Utils.isLandscape(getResources())) {
            showLandCoatOfArms(position,title,description,titleDate);
        } else {
            showPortCoatOfArms(position,title,description,titleDate);
        }
    }

    private void showLandCoatOfArms(int position, String title, String description, String titleDate) {
//        NoteLayoutFragment noteLayoutFragment = NoteLayoutFragment.newInstance(position);
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.note_fragment_container, noteLayoutFragment);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        transaction.commit();
        Activity activity = requireActivity();
        Intent intent = new Intent(activity, NoteActivityShow.class);
        intent.putExtra(ARG_INDEX, position);
        intent.putExtra(ARG_TITLE, title);
        intent.putExtra(ARG_DESCRIPTION, description);
        intent.putExtra(ARG_DATE, titleDate);
        activity.startActivity(intent);
    }

    private void showPortCoatOfArms(int position, String title, String description, String titleDate) {
        Activity activity = requireActivity();
        Intent intent = new Intent(activity, NoteActivityShow.class);
        intent.putExtra(ARG_INDEX, position);
        intent.putExtra(ARG_TITLE, title);
        intent.putExtra(ARG_DESCRIPTION, description);
        intent.putExtra(ARG_DATE, titleDate);
        activity.startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_NOTES, currentPosition);
        super.onSaveInstanceState(outState);
    }

}