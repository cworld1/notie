package com.cworld.notie;
import com.cworld.notie.util.Hitokoto;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTopAppBar(findViewById(R.id.topAppBar));
    }

    private void initTopAppBar(MaterialToolbar topAppBar) {

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

}