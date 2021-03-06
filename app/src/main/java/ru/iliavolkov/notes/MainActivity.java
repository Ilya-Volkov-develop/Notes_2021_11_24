package ru.iliavolkov.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.iliavolkov.notes.ui.AboutFragment;
import ru.iliavolkov.notes.ui.NotesFragmentListView;
import ru.iliavolkov.notes.ui.ShowFragNote;

public class MainActivity extends AppCompatActivity {

    public static String activityStr = "list";
    public static boolean pressButtonKey = false;
    public static boolean floatingBtnBool = false;
    public static FloatingActionButton floatingBtn;
    private static final String KEY_IMAGE = "personImage";
    private static final String KEY_NAME = "name";
    private long backBtnSys;
    private Toast backToast;
    private CircleImageView icon;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        addFragment(new NotesFragmentListView());
        loadIconAndName();
        initAndPressFloatingBtn();
    }

    private void addFragment(Fragment notesFragmentListView) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, notesFragmentListView)
                .commit();
    }


    //?????????????????????????? toolbar
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
    }

    //?????????????????????????? DrawerLayout
    private void initDrawer(Toolbar toolbar) {
        try {
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.navigation_view);
            View drawerHeader = navigationView.inflateHeaderView(R.layout.drawer_header);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            icon = drawerHeader.findViewById(R.id.icon);
            name = drawerHeader.findViewById(R.id.name);
            icon.setOnClickListener(v->{
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            });

            navigationView.setNavigationItemSelectedListener(item -> {
                switch (item.getItemId()){
                    case R.id.activity_about:
                        floatingBtn.setVisibility(View.GONE);
                        drawerLayout.closeDrawers();
//                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        openAboutFragment();
                        return true;
                }
                return false;
            });
        } catch (RuntimeException e) {e.printStackTrace();}
    }

    //?????????????????? ???????????? ?? ?????? ???????????????????????? ???????? ?????? ????????????????????
    private void loadIconAndName() {
        SharedPreferences pref = getSharedPreferences("MyShared", Activity.MODE_PRIVATE);
        String prefPersonImage = pref.getString(KEY_IMAGE,null);
        String prefPersonName = pref.getString(KEY_NAME,null);
        if (prefPersonImage != null) {
            Uri uri = Uri.parse(prefPersonImage);
            try { Glide.with(this).load(uri).into(icon);
            } catch (RuntimeException e){e.printStackTrace();}
        }
        if (prefPersonName != null){
            try { name.setText(prefPersonName);
            } catch (RuntimeException e) {e.printStackTrace();}
        }
    }

    //???????????????? ?????????????????? "?? ??????????????????"
    private void openAboutFragment() {
        activityStr = "about";
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("")
                .replace(R.id.fragment_container, new AboutFragment())
                .commit();
    }

    //?????????????????????????? ?? ?????????????????? ?????????????? floatingBtn
    private void initAndPressFloatingBtn() {
        floatingBtn = findViewById(R.id.floating_action_button);
        floatingBtn.setOnClickListener(v->{
            Utils.title = null;
            Utils.description = null;
            Utils.index = 0;
            activityStr = "note";
            floatingBtn.setVisibility(View.GONE);
            floatingBtnBool = true;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ShowFragNote()) //.newInstance(new NotesClass(null,null,null), ShowFragNote.indexGet)
                    .addToBackStack("")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK){
            Uri selectedImage = imageReturnedIntent.getData();
            SharedPreferences.Editor editor = getSharedPreferences("MyShared", Activity.MODE_PRIVATE).edit();
            editor.putString(KEY_IMAGE,selectedImage.toString()).apply();
            Glide.with(this).load(selectedImage).into(icon);
        }
    }
//
//    @Override
//    protected void onStop() {
//        floatingBtn.setVisibility(View.VISIBLE);
//        super.onStop();
//    }
//
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            try {
                SharedPreferences.Editor editor = getSharedPreferences("MyShared", Activity.MODE_PRIVATE).edit();
                editor.putString(KEY_NAME,name.getText().toString()).apply();
            } catch (NullPointerException e) {e.printStackTrace();}
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
        return super.dispatchTouchEvent(event);
    }
//
    @Override
    public void onBackPressed() {
        if (!Utils.isLandscape(getResources()) && activityStr.equals("list")) {
            if (backBtnSys + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                return;
            } else {
                backToast = Toast.makeText(getBaseContext(), "?????????????? ?????? ??????, ?????????? ??????????", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backBtnSys = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}