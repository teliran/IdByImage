package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private Button ratings;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ratings=findViewById(R.id.ratings);
        ratings.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        //ActionBar actionBar = this.getSupportActionBar();

        //if (actionBar != null) {
        //    actionBar.setDisplayHomeAsUpEnabled(true);
        //}
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onClick(View v) {
        if (v==ratings){
            Intent setup = new Intent(this, SetupActivity.class);
            startActivity(setup);
            finish();
        }
        if (v==back){
            Intent menu = new Intent(this, MenuActivity.class);
            startActivity(menu);
            finish();
        }
    }
}

