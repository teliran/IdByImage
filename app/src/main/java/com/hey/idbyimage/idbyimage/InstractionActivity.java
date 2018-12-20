package com.hey.idbyimage.idbyimage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InstractionActivity extends AppCompatActivity implements View.OnClickListener{
    Button back;
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instraction);
        back=findViewById(R.id.backbtn);
        start=findViewById(R.id.startbtn);
        start.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if(v.getId()==R.id.backbtn)
            intent=new Intent(this, WelcomeActivity.class);
        else if(v.getId()==R.id.backbtn);

        if(intent!=null)
            startActivity(intent);
    }
}
