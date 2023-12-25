package com.cworld.notie;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initTopAppBar(findViewById(R.id.topAppBar));
        initButtons();

        // filling the version text
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = packageInfo.versionName;
            String versionFormat = getString(R.string.version_format);
            TextView versionTextview = findViewById(R.id.app_version_textview);
            versionTextview.setText(String.format(versionFormat, version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initButtons() {
        // about app
        findViewById(R.id.btn_github).setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/cworld1/notie")))
        );
        findViewById(R.id.btn_email).setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("mailto:cworld0@qq.com")))
        );

        // donate
        findViewById(R.id.btn_alipay).setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://qr.alipay.com/fkx13845ccfsdjyuhvokt47")))
        );

        // author
        findViewById(R.id.author_cworld).setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://blog.cworld.top")))
        );
    }

    private void initTopAppBar(@NonNull MaterialToolbar topAppBar) {
        // set click listener
        topAppBar.setNavigationOnClickListener(v -> finish());
    }
}