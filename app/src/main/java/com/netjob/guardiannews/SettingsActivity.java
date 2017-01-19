package com.netjob.guardiannews;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener {


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference orderbyPref = findPreference(getString(R.string.settings_order_by_key));
            orderbyPref.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String newSummaryValue = preferences.getString(orderbyPref.getKey(), "");
            onPreferenceChange(orderbyPref, newSummaryValue);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            preference.setSummary(newValue.toString());
            return true;
        }
    }


}
