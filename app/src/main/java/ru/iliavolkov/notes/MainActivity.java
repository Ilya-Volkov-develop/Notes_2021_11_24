package ru.iliavolkov.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton floatingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAndPressFloatingBtn();

//        if (savedInstanceState == null)
            getFragmentNotes();
    }

    private void initAndPressFloatingBtn() {
        floatingBtn = findViewById(R.id.floating_action_button);
        floatingBtn.setOnClickListener(v->{
            int id;
            if (Utils.isLandscape(getResources())) id = R.id.main_fragment_container_note;
            else {
                floatingBtn.setVisibility(View.GONE);
                id = R.id.fragment_container;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(id, new ShowFragNote())
                    .addToBackStack("")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            if (Utils.isLandscape(getResources())) getFragmentNotes();
        });
    }

    private void getFragmentNotes() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new NotesFragmentListView())
                .commit();
    }

    @Override
    public void onBackPressed() {
        floatingBtn.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }

}