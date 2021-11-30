package ru.iliavolkov.notes;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class NotesFragmentListView extends Fragment {

    private ArrayList<NotesClass> arrNotes;
    private static final String KEY_INDEX = "index";
    private int index = 0;
    private int indexSave = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
        index = ShowFragNote.indexGet;
//        if (savedInstanceState != null) index = savedInstanceState.getInt(KEY_INDEX,0);

        if (Utils.isLandscape(getResources()) && arrNotes != null) showLandCoatOfArms(arrNotes.get(index),index);
    }

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = prefs.getString("arrNotes", null);
        Type type = new TypeToken<ArrayList<NotesClass>>() {}.getType();
        arrNotes = new Gson().fromJson(json, type);
        index = 0;
        try {
            for (NotesClass notes: arrNotes) {
                final int indexFinal = index;
                addViewInLinerLayoutList(notes,layoutView,indexFinal);
                index++;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void addViewInLinerLayoutList(NotesClass note, LinearLayout layoutView, int index) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout_list = inflater.inflate(R.layout.note_list, null);
        TextView title = layout_list.findViewById(R.id.titleList);
        TextView titleDate = layout_list.findViewById(R.id.titleDataList);
        TextView description = layout_list.findViewById(R.id.descriptionList);
        title.setText(note.getTitle());
        titleDate.setText(note.getDate());
        description.setText(note.getDescription());
        layout_list.setOnClickListener(v-> {
            showNote(note,index);
            if (Utils.isLandscape(getResources())) {
                NotesFragmentListView notesFragmentListView = new NotesFragmentListView();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, notesFragmentListView);
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        });
//        layout_list.setOnLongClickListener(v->{ });
        layoutView.addView(layout_list);
    }

    private void showNote(NotesClass note, int index) {
        if (Utils.isLandscape(getResources())) showLandCoatOfArms(note,index);
        else showPortCoatOfArms(note,index);
    }

    private void showLandCoatOfArms(NotesClass note, int index) {
        ShowFragNote showFragNote = ShowFragNote.newInstance(note,index);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment_container_note, showFragNote);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    private void showPortCoatOfArms(NotesClass note, int index) {
        ShowFragNote noteLayoutFragment = ShowFragNote.newInstance(note,index);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, noteLayoutFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack("");
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
////        outState.putInt(KEY_INDEX,indexSave);
//        super.onSaveInstanceState(outState);
//    }
}