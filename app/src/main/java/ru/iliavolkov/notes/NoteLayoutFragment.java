package ru.iliavolkov.notes;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteLayoutFragment extends Fragment {

    public static final String ARG_INDEX = "index";
    public static final String ARG_TITLE = "title";
    public static final String ARG_DESCRIPTION = "description";
    public static final String ARG_DATE = "date";
    private int index;
    private String title;
    private String description;
    private String date;

    public NoteLayoutFragment() {

    }

    public static NoteLayoutFragment newInstance(int index) {
        NoteLayoutFragment fragment = new NoteLayoutFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
//        args.putString(ARG_TITLE, title);
//        args.putString(ARG_DESCRIPTION, description);
//        args.putString(ARG_DATE, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
        }
        try {
            title = (String) getActivity().getIntent().getExtras().get(ARG_TITLE);
            description = (String) getActivity().getIntent().getExtras().get(ARG_DESCRIPTION);
            date = (String) getActivity().getIntent().getExtras().get(ARG_DATE);
        } catch (NullPointerException e) {}
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
        TextView titleDateText = view.findViewById(R.id.titleData);
        titleText.setText(title);
        descriptionText.setText(description);
        titleDateText.setText(date);
    }
}