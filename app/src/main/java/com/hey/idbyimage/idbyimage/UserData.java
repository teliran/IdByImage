package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hey.idbyimage.idbyimage.Utils.DataCollector;

public class UserData extends AppCompatActivity implements View.OnClickListener {
    private Button back,next;
    SharedPreferences userData;
    DataCollector dc = DataCollector.getDataCollectorInstance();

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
            final String id = dc.id(this);
            RadioGroup genderRadioGroup = findViewById(R.id.radioGroup);
            RadioButton selectedRadioGroup = findViewById(genderRadioGroup.getCheckedRadioButtonId());
            final String gender = selectedRadioGroup.getText().toString();
            try {
                EditText ageInput = findViewById(R.id.ageInput);
                int age = Integer.parseInt(ageInput.getText().toString());
                if (age < 5 || age > 100)
                    Toast.makeText(this, "Age not valid", Toast.LENGTH_SHORT).show();
                else {
                    /* -----------Data Collecting---------------
                    final String sAge = age + "";
                    SharedPreferences.Editor editor = userData.edit();
                    editor.putString("UserData", "true");
                    editor.commit();
                    //---------------------------Send Data----------------------------
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            dc.sendUserDataToServer(id, sAge, gender);
                        }
                    });
                    t.setName("Send user data");
                    t.setPriority(Thread.MAX_PRIORITY);
                    t.start();
                    try {
                        Thread.currentThread().join(1000);
                    } catch (InterruptedException e) {
                        Log.e("Thread Error: ", t.getName());
                    }
                    */
                    ////////////////////////////////////////////////////////////////////
                    Intent pinIntent = new Intent(this, PinLockActivity.class);
                    startActivity(pinIntent);
                    finish();
                }
            }catch (NumberFormatException e){
                Toast.makeText(this, "Age is not valid", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
