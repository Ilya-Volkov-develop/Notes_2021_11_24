package ru.iliavolkov.notes;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
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

public class ShowFragNote extends Fragment {

    public static final String ARG_INDEX = "index";
    public static final String ARG_TITLE = "title";
    public static final String ARG_DESCRIPTION = "description";
    public static int indexGet;
    private String titleGet;
    private String descriptionGet;
    private EditText titleText;
    private EditText descriptionText;
    private ArrayList<NotesClass> arrNotes;

    public ShowFragNote() {

    }

    public static ShowFragNote newInstance(NotesClass note, int index) {
        ShowFragNote fragment = new ShowFragNote();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        args.putString(ARG_TITLE, note.getTitle());
        args.putString(ARG_DESCRIPTION, note.getDescription());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            indexGet = getArguments().getInt(ARG_INDEX);
            titleGet = getArguments().getString(ARG_TITLE);
            descriptionGet = getArguments().getString(ARG_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleText = view.findViewById(R.id.title);
        descriptionText = view.findViewById(R.id.description);
        titleText.setText(titleGet);
        descriptionText.setText(descriptionGet);
    }

    @Override
    public void onStop() {
        saveNote();
        super.onStop();
    }

    private void saveNote() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();

        TextView title = requireActivity().findViewById(R.id.title);
        TextView description = requireActivity().findViewById(R.id.description);

        Date current = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        String message = formatter.format(current);

        NotesClass notes = new NotesClass(title.getText().toString(), description.getText().toString(), message);

        String json = prefs.getString("arrNotes", null);
        Type type = new TypeToken<ArrayList<NotesClass>>() {}.getType();

        if (titleGet == null) titleGet = "";
        if (descriptionGet == null) descriptionGet = "";

        if (json == null) arrNotes = new ArrayList<>();
        else arrNotes = gson.fromJson(json, type);

        if (!title.getText().toString().equals(titleGet) || !description.getText().toString().equals(descriptionGet)) {
            if (title.getText().toString().replace(" ","").equals("") && description.getText().toString().replace(" ","").equals("")
                    && (!titleGet.equals("") || !descriptionGet.equals(""))) arrNotes.remove(indexGet);
            else if (titleGet.equals("") && descriptionGet.equals("")) arrNotes.add(0,notes);
            else if (!titleGet.equals("") || !descriptionGet.equals("")) {
                arrNotes.remove(indexGet);
                arrNotes.add(0,notes);
            }
            title.setText(""); description.setText("");
            json = gson.toJson(arrNotes);
            editor.putString("arrNotes", json);
            editor.apply();
        }
    }
}