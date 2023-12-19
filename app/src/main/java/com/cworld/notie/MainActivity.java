package com.cworld.notie;

import com.cworld.notie.adapter.NoteItemAdapter;
import com.cworld.notie.adapter.NoteModel;
import com.cworld.notie.util.Hitokoto;
import com.cworld.notie.util.NoteHelper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    NoteItemAdapter noteAdapter;
    NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        initTopAppBar(findViewById(R.id.topAppBar));
        initAppDrawer(findViewById(R.id.navigationDrawerView));

        findViewById(R.id.floatingCreateButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, EditActivity.class);
            startActivity(intent);
        });

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_main_bar, menu);
        return true;
    }

    private void initRecyclerView() {
        noteHelper = new NoteHelper(getApplicationContext(), "notes");
        List<NoteModel> noteList = noteHelper.getAllNotes();

        // Sort by last edit time
//        noteList.sort((note1, note2) -> {
//            Date time1 = note1.getEditTime();
//            Date time2 = note2.getEditTime();
//            return time2.compareTo(time1);
//        });

        // Initialize and set up the RecyclerView
        RecyclerView noteRecyclerView = findViewById(R.id.noteRecyclerView);
        noteAdapter = new NoteItemAdapter(noteList);
        noteAdapter.setOnItemClickListener(note -> {
            Intent intent = new Intent();
            intent.setClass(this, EditActivity.class);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            // intent.putExtra("editTime", note.getEditTime().getTime());
            startActivity(intent);
        });
        noteRecyclerView.setAdapter(noteAdapter);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initTopAppBar(@NonNull MaterialToolbar topAppBar) {
        setSupportActionBar(topAppBar);
        boolean isTablet = getResources().getConfiguration().smallestScreenWidthDp >= 600;
        // set click listener
        if (!isTablet) {
            topAppBar.setNavigationOnClickListener(v -> {
                DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                drawerLayout.open();
            });
        }
        topAppBar.setOnMenuItemClickListener(item -> {
            final int itemId = item.getItemId();
            if (itemId == R.id.item_sort) {

            } else if (itemId == R.id.item_select) {
            } else return false;
            return true;
        });

        // Add poem to title bar
        Executors.newSingleThreadExecutor().execute(() -> {
            String result = Hitokoto.fetchPoem();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                if (result == null) {
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            R.string.fetch_poem_alert,
                            Snackbar.LENGTH_SHORT
                    ).show();
                    return;
                }
                if (isTablet) {
                    TextView drawerHeaderDesView = findViewById(R.id.drawerHeaderDescription);
                    drawerHeaderDesView.setText(result);
                } else topAppBar.setSubtitle(result);
            });
        });
    }

    private void initAppDrawer(@NonNull NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent;
            if (itemId == R.id.all_item) {

            } else if (itemId == R.id.website_item) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.repo_url)));
                startActivity(intent);
            } else if (itemId == R.id.settings_item) {
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.about_item) {
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
            } else return false;
            return true;
        });
    }

}