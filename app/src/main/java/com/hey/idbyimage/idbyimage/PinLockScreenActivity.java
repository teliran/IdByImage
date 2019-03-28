package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PinLockScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private Button submit;
    private EditText firstPin, secondPin, thirdPin, fourthPin;
    private SharedPreferences pinPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_lock_screen);
        init();
        initPinSequence();


    }

    private void init(){
        firstPin =  findViewById(R.id.first_pin);
        secondPin =  findViewById(R.id.second_pin);
        thirdPin =  findViewById(R.id.third_pin);
        fourthPin = findViewById(R.id.fourth_pin);
        submit=findViewById(R.id.submitBtn);
        submit.setEnabled(false);
        submit.setOnClickListener(this);
        pinPref=getSharedPreferences("pinPref", Context.MODE_PRIVATE);
    }

    private void initPinSequence(){
        firstPin.requestFocus();
        firstPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                secondPin.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (checkPin()){
                    submit.setEnabled(true);
                }
            }
        });

        secondPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                thirdPin.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (checkPin()){
                    submit.setEnabled(true);
                }
            }
        });
        thirdPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fourthPin.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (checkPin()){
                    submit.setEnabled(true);
                }
            }
        });
        fourthPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                submit.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (checkPin()){
                    submit.setEnabled(true);
                }
            }
        });


    }

    public boolean validatePin(){
        try{
            String pin = firstPin.getText().toString()+
                    secondPin.getText().toString()+
                    thirdPin.getText().toString()+
                    fourthPin.getText().toString();
            String validate =pinPref.getString("PIN","");
            if (pin.equals(validate))
                return true;
            return false;
        }catch (Exception e){
            return false;
        }
    }



    public boolean checkPin() {
        try{
            String pin = firstPin.getText().toString()+
                    secondPin.getText().toString()+
                    thirdPin.getText().toString()+
                    fourthPin.getText().toString();
            if (pin.length()>=4){
                return true;
            }
            pin=null;
            return false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v==submit){
            if (validatePin()) {
                Toast.makeText(this, "Pin matched", Toast.LENGTH_SHORT).show();
                this.finish();
            }
            else{
                firstPin.setText("");
                secondPin.setText("");
                thirdPin.setText("");
                fourthPin.setText("");
                submit.setEnabled(false);
                Toast.makeText(this,"Pin did not match",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
