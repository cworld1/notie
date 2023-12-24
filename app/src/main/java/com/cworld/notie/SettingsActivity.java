package com.cworld.notie;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cworld.notie.fragment.SettingsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        // init components
        initTopAppBar(findViewById(R.id.topAppBar));

        // get fragments
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }

    private void initTopAppBar(MaterialToolbar topAppBar) {
        topAppBar.setNavigationOnClickListener(v -> finish());
    }
}