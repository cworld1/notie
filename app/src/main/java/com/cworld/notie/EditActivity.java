package com.cworld.notie;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.color.DynamicColors;

public class EditActivity extends AppCompatActivity {
    MaterialToolbar topAppBar;
    BottomAppBar bottomAppBar;
    EditText editHeader;
    EditText editBody;
    String originTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        topAppBar = findViewById(R.id.topAppBar);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        editHeader = findViewById(R.id.editHeader);
        editBody = findViewById(R.id.editBody);

        initTopAppBar();
        initBottomAppBar();
        originTitle = getIntent().getStringExtra("title");
        initHeader(originTitle);
        initBody(getIntent().getStringExtra("content"));
    }

    private void initTopAppBar() {
        topAppBar.setNavigationOnClickListener(v -> finish());
        topAppBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.item_done) {
                editHeader.clearFocus();
                editBody.clearFocus();
            } else if (itemId == R.id.item_share) {
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
            } else return false;
            return true;
        });
    }

    private void initHeader(String title) {
        // set title content
        editHeader.setText(title);
        if (title != null) {
            topAppBar.setTitle(title);
        }

        // set header focus with action
        editHeader.setOnFocusChangeListener((v, hasFocus) -> {
            setEditStat(hasFocus);
        });

        // watch text change (sync with topappbar)
        editHeader.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initBody(String content) {
        // set body content
        editBody.setText(content);

        // set header focus with action
        editBody.setOnFocusChangeListener((v, hasFocus) -> setEditStat(hasFocus));
    }

    private void initBottomAppBar() {
        bottomAppBar.setNavigationOnClickListener(item -> {
            int itemId = item.getId();
            if (itemId == R.id.actionCommand) {

            }
        });
    }
}