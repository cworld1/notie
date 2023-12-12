package com.cworld.notie;

import com.cworld.notie.adapter.NoteItemAdapter;
import com.cworld.notie.adapter.NoteModel;
import com.cworld.notie.util.Hitokoto;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        initTopAppBar(findViewById(R.id.topAppBar));
        initAppDrawer(findViewById(R.id.navigationDrawerView));

        findViewById(R.id.floatingCreateButton).setOnClickListener(v -> {
        });

        List<Object> noteList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            NoteModel note = new NoteModel("Note No." + i, "A note of No. " + i);
            noteList.add(note);
        }
        // Initialize and set up the RecyclerView
        RecyclerView noteRecyclerView = findViewById(R.id.noteRecyclerView);
        NoteItemAdapter noteAdapter = new NoteItemAdapter(noteList);
        noteRecyclerView.setAdapter(noteAdapter);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initTopAppBar(MaterialToolbar topAppBar) {
        // set click listener
        topAppBar.setNavigationOnClickListener(v -> {
            DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
            drawerLayout.open();
        });

        // Add poem to title bar
        Executors.newSingleThreadExecutor().execute(() -> {
            String result = Hitokoto.fetchPoem();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                if (result != null) {
                    topAppBar.setSubtitle(result);
                } else {
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            "Failed to fetch poem",
                            Snackbar.LENGTH_SHORT
                    ).show();
                }
            });
        });
    }

    private void initAppDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.website_item) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/cworld1/notie"));
                startActivity(intent);
            } else if (item.getItemId() == R.id.about_item) {
            } else return false;
            return true;
        });
    }

}