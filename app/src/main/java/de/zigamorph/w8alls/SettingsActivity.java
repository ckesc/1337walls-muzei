package de.zigamorph.w8alls;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

public class SettingsActivity extends ActionBarActivity {

    private SharedPreferences prefs;
    private boolean pref_cyclemode_start;
    private String pref_intervalpicker_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().
                    replace(android.R.id.content, new PrefsFragment())
                    .commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.pref_cyclemode_start = prefs.getBoolean(getString(R.string.pref_cyclemode_key), true);
        this.pref_intervalpicker_start = prefs.getString(
                getString(R.string.pref_intervalpicker_key),
                getString(R.string.pref_intervalpicker_defaultvalue));
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

         /* show correct version name & copyright year */
            try {
                findPreference(getString(R.string.pref_about_key))
                        .setSummary(getString(R.string.pref_about_summary,
                                getActivity().getPackageManager()
                                        .getPackageInfo(getActivity().getPackageName(), 0).versionName,
                                Calendar.getInstance().get(Calendar.YEAR)));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.setFitsSystemWindows(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        boolean shouldRefresh = false;
        if (pref_cyclemode_start != prefs.getBoolean(getString(R.string.pref_cyclemode_key), true)
                || !(pref_intervalpicker_start.equals(prefs.getString(
                getString(R.string.pref_intervalpicker_key),
                getString(R.string.pref_intervalpicker_defaultvalue))))) {
            shouldRefresh = true;
        }

        startService(new Intent(SettingsActivity.this, MainActivity.class)
                .putExtra(MainActivity.EXTRA_SHOULD_REFRESH, shouldRefresh)
                .setAction(MainActivity.ACTION_NEW_SETTINGS));
        super.onBackPressed();
    }
}