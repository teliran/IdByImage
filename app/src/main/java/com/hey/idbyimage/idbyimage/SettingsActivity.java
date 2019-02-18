package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupSharedPreferences();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences settingsPref=getSharedPreferences("settingsPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settingsPref.edit();
        if (key.equals("pref_num_images_3x3")) {
            editor.putInt("numOfImagesToShow", 9);
        }
        else if(key.equals("pref_num_images_2x3"))
            editor.putInt("numOfImagesToShow", 6);
        else if(key.equals("pref_num_images_2"))
            editor.putInt("numOfImagesToSelect", 2);
        else if(key.equals("pref_num_images_3"))
            editor.putInt("numOfImagesToSelect", 3);
        else if(key.equals("pref_num_images_4"))
            editor.putInt("numOfImagesToSelect", 4);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}

