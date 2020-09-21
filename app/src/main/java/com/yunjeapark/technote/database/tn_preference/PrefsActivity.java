package com.yunjeapark.technote.database.tn_preference;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.yunjeapark.technote.R;

public class PrefsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}