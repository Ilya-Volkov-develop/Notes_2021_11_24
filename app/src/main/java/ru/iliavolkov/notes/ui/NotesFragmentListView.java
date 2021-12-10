package ru.iliavolkov.notes.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.iliavolkov.notes.MainActivity;
import ru.iliavolkov.notes.R;
import ru.iliavolkov.notes.Utils;
import ru.iliavolkov.notes.data.NotesData;

public class NotesFragmentListView extends Fragment  {

    private ArrayList<NotesData> arrNotes;
    private View view;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.recycle_view_lines);
        arrNotes = getArrNotes();
        initRecycleView(recyclerView,arrNotes);
        return view;
    }

    private void initRecycleView(RecyclerView recyclerView, ArrayList<NotesData> notes) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        NotesAdapter adapter = new NotesAdapter(notes);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            if (!Utils.isLandscape(getResources()))
                MainActivity.floatingBtn.setVisibility(View.GONE);
            if (Utils.isLandscape(getResources()))
                setFragment(arrNotes.get(position),position,R.id.main_fragment_container_note);
            else {
                MainActivity.activityStr = "note";
                setFragment(arrNotes.get(position),position,R.id.fragment_container);
            }
        });
        adapter.setItemLongClickListener((v, position)->{
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch(item.getItemId()) {
                    case R.id.action_popup_delete:
                        arrNotes.remove(arrNotes.get(position));
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        String json = new Gson().toJson(arrNotes);
                        editor.putString("arrNotes", json).apply();
                        initRecycleView(recyclerView,notes);
                        return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    private ArrayList<NotesData> getArrNotes() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = prefs.getString("arrNotes", null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<NotesData>>() {}.getType();
            return new Gson().fromJson(json, type);
        } else return new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    private void setFragment(NotesData note, int index, int id) {
        ShowFragNote showFragNote = new ShowFragNote();//.newInstance(note,index);
        Utils.title = note.getTitle();
        Utils.description = note.getDescription();
        Utils.index = index;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id, showFragNote);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (!Utils.isLandscape(getResources())) transaction.addToBackStack("");
        transaction.commit();
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        MainActivity.floatingBtn.setVisibility(View.VISIBLE);
//    }
}