package com.company.moviesapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    public SettingsFragment() {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        addPreferencesFromResource ( R.xml.preferences );

        Preference orderBy = findPreference ( "sortOrder" );
        bindPreferenceSummaryToValue ( orderBy );
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener ( this );
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences ( preference.getContext () );
        String preferenceString = sharedPreferences.getString ( preference.getKey (), "" );
        onPreferenceChange ( preference, preferenceString );
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString ();
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue ( stringValue );
            if (prefIndex >= 0) {
                CharSequence[] labels = listPreference.getEntries ();
                preference.setSummary ( labels[prefIndex] );
            }
        } else {
            preference.setSummary ( stringValue );
        }
        return true;
    }
}
