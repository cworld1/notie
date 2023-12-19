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
import android.view.ActionMode;
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
    ActionMode actionMode;
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
                initActionMode(topAppBar);
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

    private void initActionMode(View view) {
        if (actionMode != null) {
            return;
        }

        // Start the CAB using the ActionMode.Callback defined earlier.
        actionMode = startActionMode(new ActionMode.Callback() {

            // Called when the action mode is created. startActionMode() is called.
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items.
                mode.getMenuInflater().inflate(R.menu.contextual_action_bar, menu);
                return true;
            }

            // Called each time the action mode is shown. Always called after
            // onCreateActionMode, and might be called multiple times if the mode
            // is invalidated.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                view.setVisibility(View.GONE);
                noteAdapter.setSelectionMode(true);
                return true; // Return false if nothing is done.
            }

            // Called when the user selects a contextual menu item.
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_delete) {
                    List<NoteModel> notes = noteAdapter.getSelectedItems();
                    for (NoteModel note : notes) {
                        NoteHelper.deleteNote(note);
                    }
                    initRecyclerView();
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            "Delete successful!",
                            Snackbar.LENGTH_SHORT
                    ).show();
                    mode.finish(); // Action picked, so close the CAB.
                } else return false;
                return true;
            }

            // Called when the user exits the action mode.
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                view.setVisibility(View.VISIBLE);
                noteAdapter.setSelectionMode(false);
                noteAdapter.clearSelection();
                actionMode = null;
            }
        });
    }
}