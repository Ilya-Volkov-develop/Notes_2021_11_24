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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.iliavolkov.notes.MainActivity;
import ru.iliavolkov.notes.R;
import ru.iliavolkov.notes.Utils;

public class NotesFragmentListView extends Fragment  {

    private ArrayList<NotesClass> arrNotes;
    private View view;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.recycle_view_lines);
        arrNotes = getArrNotes();
        initRecycleView(recyclerView,arrNotes);
        return view;
    }

    private void initRecycleView(RecyclerView recyclerView, ArrayList<NotesClass> notes) {
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

    private ArrayList<NotesClass> getArrNotes() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = prefs.getString("arrNotes", null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<NotesClass>>() {}.getType();
            return new Gson().fromJson(json, type);
        } else return new ArrayList<>();
    }

//    private void listItemOnClick(NotesClass note, int index) {

//    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initList(view);
//
//        if (!Utils.isLandscape(getResources())) {
//
//            MainActivity.floatingBtn.setVisibility(View.VISIBLE);
//            Utils.portable = true;
//        }
//        if (Utils.isLandscape(getResources())) MainActivity.floatingBtn.setVisibility(View.GONE);
//        if (Utils.isLandscape(getResources()) && Utils.portable && arrNotes != null) {
//            Utils.portable = false;
//            try {
//                setFragment(arrNotes.get(0),0,R.id.main_fragment_container_note);
//            }catch (IndexOutOfBoundsException e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void initList(View view) {
//        LinearLayout layoutView = (LinearLayout) view;
//        arrNotes = getArrNotes();
//        int index = 0;
//        if (arrNotes != null) {
//            for (NotesClass notes: arrNotes) {
//                final int indexFinal = index;
//                addViewInLinerLayoutList(notes,layoutView,indexFinal);
//                index++;
//            }
//        }
//    }
//
//
//    private void addViewInLinerLayoutList(NotesClass note, LinearLayout layoutView, int index) {
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View layout_list = inflater.inflate(R.layout.note_item, null);
//        TextView title = layout_list.findViewById(R.id.titleList);
//        TextView titleDate = layout_list.findViewById(R.id.titleDataList);
//        TextView description = layout_list.findViewById(R.id.descriptionList);
//        title.setText(note.getTitle());
//        titleDate.setText(note.getDate());
//        description.setText(note.getDescription());
//        layout_list.setOnClickListener(v-> listItemOnClick(note, index));
//        layout_list.setOnLongClickListener(v->{

//        });
//        layoutView.addView(layout_list);
//    }
//

//
    private void setFragment(NotesClass note, int index, int id) {
        ShowFragNote showFragNote = ShowFragNote.newInstance(note,index);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id, showFragNote);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (!Utils.isLandscape(getResources())) transaction.addToBackStack("");
        transaction.commit();
    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        MainActivity.floatingBtn.setVisibility(View.VISIBLE);
//    }
}