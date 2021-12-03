package ru.iliavolkov.notes.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.iliavolkov.notes.MainActivity;
import ru.iliavolkov.notes.NotesClass;
import ru.iliavolkov.notes.R;
import ru.iliavolkov.notes.Utils;

public class NotesFragmentListView extends Fragment  {

    private ArrayList<NotesClass> arrNotes;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);

        if (!Utils.isLandscape(getResources())) {

            MainActivity.floatingBtn.setVisibility(View.VISIBLE);
            Utils.portable = true;
        }
        if (Utils.isLandscape(getResources())) MainActivity.floatingBtn.setVisibility(View.GONE);
        if (Utils.isLandscape(getResources()) && Utils.portable && arrNotes != null) {
            Utils.portable = false;
            try {
                setFragment(arrNotes.get(0),0,R.id.main_fragment_container_note);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = prefs.getString("arrNotes", null);
        Type type = new TypeToken<ArrayList<NotesClass>>() {}.getType();
        arrNotes = new Gson().fromJson(json, type);
        int index = 0;
        if (arrNotes != null) {
            for (NotesClass notes: arrNotes) {
                final int indexFinal = index;
                addViewInLinerLayoutList(notes,layoutView,indexFinal);
                index++;
            }
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
        layout_list.setOnClickListener(v-> listItemOnClick(note, index));
        layout_list.setOnLongClickListener(v->{
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch(item.getItemId()) {
                    case R.id.action_popup_delete:
                        layoutView.removeView(layout_list);
                        arrNotes.remove(note);
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        String json = new Gson().toJson(arrNotes);
                        editor.putString("arrNotes", json).apply();
                        return true;
                }
                return false;
            });
            popupMenu.show();
            return true;
        });
        layoutView.addView(layout_list);
    }

    private void listItemOnClick(NotesClass note, int index) {
        if (!Utils.isLandscape(getResources()))
            MainActivity.floatingBtn.setVisibility(View.GONE);
        if (Utils.isLandscape(getResources()))
            setFragment(note,index,R.id.main_fragment_container_note);
        else {
            MainActivity.activityStr = "note";
            setFragment(note,index,R.id.fragment_container);
        }
    }

    private void setFragment(NotesClass note, int index, int id) {
        ShowFragNote showFragNote = ShowFragNote.newInstance(note,index);
//        Utils.title = note.getTitle();
//        Utils.description = note.getDescription();
//        Utils.index = index;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id, showFragNote);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack("");
        transaction.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}