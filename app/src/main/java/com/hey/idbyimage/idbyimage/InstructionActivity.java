package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InstructionActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences userPref;
    private Button back;
    private Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instraction);
        back=findViewById(R.id.backbtn);
        start=findViewById(R.id.startbtn);
        start.setOnClickListener(this);
        back.setOnClickListener(this);
        pref =getSharedPreferences("pinPref", Context.MODE_PRIVATE);
        userPref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if(v.getId()==R.id.backbtn)
            intent = new Intent(this,WelcomeActivity.class);
        else if(v.getId()==R.id.startbtn) {
            if(pref.getAll().size()==0) {
                if(userPref.getAll().size()==0)
                    intent = new Intent(this, UserData.class);
                else
                    intent=new Intent(this,PinLockActivity.class);
            }
            else {
                intent = new Intent(this, SetupActivity.class);
            }
        }
        if(intent!=null) {
            startActivity(intent);
            finish();
        }
    }
}
