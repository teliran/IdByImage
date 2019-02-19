package com.hey.idbyimage.idbyimage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements  View.OnClickListener{
    private Button trybtn;
    private Button activatebtn;
    private Button settingsbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        trybtn=findViewById(R.id.trybtn);
        activatebtn=findViewById(R.id.activatebtn);
        settingsbtn=findViewById(R.id.settingsbtn);
        trybtn.setOnClickListener(this);
        activatebtn.setOnClickListener(this);
        settingsbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==trybtn)
            startActivity(new Intent(this,LockScreenActivity.class));
        else if(v==activatebtn){
            startActivity(new Intent(this, LockScreenActivity.class));
            finish();
        }
        else if(v==settingsbtn){
            startActivity(new Intent(this,SettingsActivity.class));
        }
    }
}
