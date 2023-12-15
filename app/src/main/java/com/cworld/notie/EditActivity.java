package com.cworld.notie;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.color.DynamicColors;

public class EditActivity extends AppCompatActivity {
    String originTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        initTopAppBar(topAppBar);
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


        initHeader(findViewById(R.id.editHeader), topAppBar);
    }

    private void initHeader(@NonNull EditText editText, @NonNull MaterialToolbar topAppBar) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String title;
                if (s.length() == 0)
                    title = getString(R.string.edit_bar_name);
                else
                    title = s.toString();
                topAppBar.setTitle(title);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
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