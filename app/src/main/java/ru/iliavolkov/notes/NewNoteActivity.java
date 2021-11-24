package ru.iliavolkov.notes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class NewNoteActivity extends AppCompatActivity {

    private EditText description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_layout);

        NestedScrollView scrollEditText = findViewById(R.id.scrollEditText);
        description = findViewById(R.id.description);
        scrollEditText.setOnClickListener(v->{
            Toast.makeText(this,"Yf vtyz yf;fkb", Toast.LENGTH_SHORT).show();
            description.requestFocus();
//            description.post(() -> {
//                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInputFromWindow(
//                        description.getApplicationWindowToken(),InputMethodManager.SHOW_IMPLICIT, 0);
//                description.setFocusableInTouchMode(true);
//
//            });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TextView title = findViewById(R.id.title);
        TextView titleDate = findViewById(R.id.titleData);
        TextView description = findViewById(R.id.description);
        Notes notes = new Notes(title.getText().toString(), description.getText().toString(), titleDate.getText().toString());
        ArrayList<Notes> arrayList = new ArrayList<>();
        arrayList.add(notes);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("arrNotes", json);
        editor.apply();
    }
}
