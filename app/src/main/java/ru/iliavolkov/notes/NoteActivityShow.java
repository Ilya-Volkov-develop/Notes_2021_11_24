package ru.iliavolkov.notes;



import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class NoteActivityShow extends AppCompatActivity {
    public static final String ARG_INDEX = "index";
    public static final String ARG_TITLE = "title";
    public static final String ARG_DESCRIPTION = "description";
    public static final String ARG_DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

//        if (Utils.isLandscape(getResources())) {
//            finish();
//            return;
//        }

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.note_fragment_container, NoteLayoutFragment.newInstance(getIntent().getExtras().getInt(ARG_INDEX)))
                    .commit();
    }
}