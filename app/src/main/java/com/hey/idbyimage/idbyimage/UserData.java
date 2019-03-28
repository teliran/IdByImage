package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserData extends AppCompatActivity implements View.OnClickListener {
    private Button back,next;
    SharedPreferences userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        back=findViewById(R.id.backBtn);
        back.setOnClickListener(this);
        next=findViewById(R.id.nextBtn);
        next.setOnClickListener(this);
        userData=getSharedPreferences("UserData", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        if(v==back){
            Intent instraction = new Intent(this,InstructionActivity.class);
            startActivity(instraction);
            finish();
        }
        else if (v==next){
            EditText ageInput = findViewById(R.id.ageInput);
            int age = Integer.parseInt(ageInput.getText().toString());
            if (age<5 || age>100)
                Toast.makeText(this,"Age not valid",Toast.LENGTH_SHORT).show();
            else{
                SharedPreferences.Editor editor = userData.edit();
                editor.putString("UserData", "true");
                editor.commit();
                Intent pinIntent = new Intent(this,PinLockActivity.class);
                startActivity(pinIntent);
                finish();
            }
        }
    }
}
