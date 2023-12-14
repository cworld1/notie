package com.cworld.notie;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.color.DynamicColors;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        initTopAppBar(findViewById(R.id.topAppBar));
        initBottomAppBar(findViewById(R.id.bottomAppBar));

        String title = null;
        if (getIntent().hasExtra("title")) {
            title = getIntent().getStringExtra("title");
        }
        String content = null;
        if (getIntent().hasExtra("content")) {
            content = getIntent().getStringExtra("content");
        }
        EditText titleView = findViewById(R.id.editHeader);
        EditText contentView = findViewById(R.id.editBody);
        titleView.setText(title);
        contentView.setText(content);
    }

    private void initTopAppBar(@NonNull MaterialToolbar topAppBar) {
        topAppBar.setNavigationOnClickListener(v -> finish());
    }

    private void initBottomAppBar(@NonNull BottomAppBar bottomAppBar) {
        bottomAppBar.setNavigationOnClickListener(item -> {
            int itemId = item.getId();
            if (itemId == R.id.actionCommand) {
                
            }
        });
    }
}