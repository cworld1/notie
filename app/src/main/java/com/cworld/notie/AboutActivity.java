package com.cworld.notie;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        initTopAppBar(findViewById(R.id.topAppBar));

        // filling the version text
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = packageInfo.versionName;
            String versionFormat = getResources().getString(R.string.version_format);
            TextView versionTextview = findViewById(R.id.app_version_textview);
            versionTextview.setText(String.format(versionFormat, version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initTopAppBar(@NonNull MaterialToolbar topAppBar) {
        // set click listener
        topAppBar.setNavigationOnClickListener(v -> finish());
    }
}