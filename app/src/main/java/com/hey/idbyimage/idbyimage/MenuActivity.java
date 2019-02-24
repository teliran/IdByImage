package com.hey.idbyimage.idbyimage;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements  View.OnClickListener{
    private Button trybtn;
    private Button activatebtn;
    private Button settingsbtn;
    private Button instructbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        trybtn=findViewById(R.id.trybtn);
        activatebtn=findViewById(R.id.activatebtn);
        settingsbtn=findViewById(R.id.settingsbtn);
        instructbtn = findViewById(R.id.HelpBtn);
        trybtn.setOnClickListener(this);
        activatebtn.setOnClickListener(this);
        settingsbtn.setOnClickListener(this);
        instructbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==trybtn)
            startActivity(new Intent(this,LockScreenActivity.class));
        else if(v==activatebtn){
            //TODO: if it is enabled, disable and stop the kiosk option, if disabled enable it...
        }
        else if(v==settingsbtn){
            startActivity(new Intent(this,SettingsActivity.class));
        }
        else if (v==instructbtn){
            popDialog();
        }
    }

    private void popDialog(){
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Instructions");
        ad.setMessage("These are some instructions");
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        //ad.create();
        ad.show();
    }
}
