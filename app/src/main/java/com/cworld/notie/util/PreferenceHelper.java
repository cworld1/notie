package com.cworld.notie.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

public class PreferenceHelper {
    private final SharedPreferences sharedPreferences;

    public PreferenceHelper(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // editor
    public final int getLineHeight() {
        return sharedPreferences.getInt("line_height", 10);
    }

    // markdown
    public final boolean getPreviewEnabled() {
        return sharedPreferences.getBoolean("markdown_preview", false);
    }
    public final boolean getHighlightEnabled() {
        return sharedPreferences.getBoolean("markdown_highlight", true);
    }

    // others
    public static void clickItem(@NonNull Preference preference, Context context, Class<?> cls) {
        preference.setOnPreferenceClickListener(preference1 -> {
            context.startActivity(new Intent(context, cls));
            return true;
        });
    }

    public static void clickItem(@NonNull Preference preference, Context context, String url) {
        preference.setOnPreferenceClickListener(preference1 -> {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        });
    }
}
