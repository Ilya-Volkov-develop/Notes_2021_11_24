package ru.iliavolkov.notes.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.iliavolkov.notes.MainActivity;
import ru.iliavolkov.notes.R;
import ru.iliavolkov.notes.Utils;
import ru.iliavolkov.notes.data.NotesData;

public class ShowFragNote extends Fragment {

    private int indexGet = 0;
    private String titleGet = null;
    private String descriptionGet = null;


    public ShowFragNote() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        saveNote();
        titleGet = Utils.title;
        descriptionGet = Utils.description;
        indexGet = Utils.index;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_layout, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText titleText = view.findViewById(R.id.title);
        EditText descriptionText = view.findViewById(R.id.description);
        titleText.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && Math.abs(count - before) == 1) {
//                    Toast.makeText(getContext(),s+" "+start + " " + before + " " + count,Toast.LENGTH_SHORT).show();
                    MainActivity.pressButtonKey = true;
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
        descriptionText.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && Math.abs(count - before) == 1) {
//                    Toast.makeText(getContext(),s+" "+start + " " + before + " " + count,Toast.LENGTH_SHORT).show();
                    MainActivity.pressButtonKey = true;
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
        titleText.setText(titleGet);
        descriptionText.setText(descriptionGet);
        MainActivity.pressButtonKey = false;
    }

    //    public static final String ARG_INDEX = "index";
//    public static final String ARG_TITLE = "title";
//    public static final String ARG_DESCRIPTION = "description";


//
//    public ShowFragNote() {}
//
//    public static ShowFragNote newInstance(NotesClass note, int index) {
//        ShowFragNote fragment = new ShowFragNote();
//        Bundle args = new Bundle();
//        args.putInt(ARG_INDEX, index);
//        args.putString(ARG_TITLE, note.getTitle());
//        args.putString(ARG_DESCRIPTION, note.getDescription());
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            saveNote();
//            indexGet = getArguments().getInt(ARG_INDEX);
//            titleGet = getArguments().getString(ARG_TITLE);
//            descriptionGet = getArguments().getString(ARG_DESCRIPTION);
//        }
//    }
//


//

    private void saveNote() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();

        TextView title = requireActivity().findViewById(R.id.title);
        TextView description = requireActivity().findViewById(R.id.description);

        Date current = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        String message = formatter.format(current);
        if (title != null || description != null) {
            NotesData notes = new NotesData(title.getText().toString(), description.getText().toString(), message);

            String json = prefs.getString("arrNotes", null);
            Type type = new TypeToken<ArrayList<NotesData>>() {
            }.getType();

            ArrayList<NotesData> arrNotes;
            if (json == null) arrNotes = new ArrayList<>();
            else arrNotes = gson.fromJson(json, type);

            if (MainActivity.floatingBtnBool) {
                if (!title.getText().toString().equals("") || !description.getText().toString().equals(""))
                    arrNotes.add(0, notes);
            } else if (MainActivity.pressButtonKey) {
                if (title.getText().toString().replace(" ", "").equals("") && description.getText().toString().replace(" ", "").equals(""))
                    arrNotes.remove(indexGet);
                else { //if (!titleGet.equals("") || !descriptionGet.equals(""))
                    arrNotes.remove(indexGet);
                    arrNotes.add(0, notes);
                }
                try {
                    if (Utils.isLandscape(getResources())) {
                        NotesFragmentListView notesFragmentListView = new NotesFragmentListView();
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container, notesFragmentListView);
                        transaction.commit();
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }

            MainActivity.floatingBtnBool = false;
            MainActivity.pressButtonKey = false;
            json = gson.toJson(arrNotes);
            editor.putString("arrNotes", json);
            editor.apply();
            title.setText(null);
            description.setText(null);
        }
    }

    @Override
    public void onStop() {
        if (!Utils.isLandscape(getResources())){
            MainActivity.activityStr = "list";
        }
        saveNote();
        MainActivity.floatingBtn.setVisibility(View.VISIBLE);
        super.onStop();
    }
}