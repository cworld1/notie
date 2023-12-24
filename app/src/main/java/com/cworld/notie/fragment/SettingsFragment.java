package com.cworld.notie.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.cworld.notie.AboutActivity;
import com.cworld.notie.R;
import com.cworld.notie.util.PreferenceHelper;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
