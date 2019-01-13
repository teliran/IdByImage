package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PinLockActivity extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences pinPref;

    private Button nextBtn,backBtn;
    private EditText firstPin, secondPin, thirdPin, fourthPin;
    private String pin,defined;
    private boolean firstScreen;

    public SharedPreferences getPinPref() {
        return pinPref;
    }

    public Button getNextBtn() {
        return nextBtn;
    }

    public Button getBackBtn() {
        return backBtn;
    }

    public EditText getFirstPin() {
        return firstPin;
    }

    public EditText getSecondPin() {
        return secondPin;
    }

    public EditText getThirdPin() {
        return thirdPin;
    }

    public EditText getFourthPin() {
        return fourthPin;
    }

    public String getPin() {
        return pin;
    }

    public String getDefined() {
        return defined;
    }

    public boolean isFirstScreen() {
        return firstScreen;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setDefined(String defined) {
        this.defined = defined;
    }

    public void setFirstScreen(boolean firstScreen) {
        this.firstScreen = firstScreen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_lock);
        init();
        initPinSequence();
        firstScreen=true;
        pinPref=getSharedPreferences("pinPref", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view==nextBtn) {
            if(firstScreen) {
                checkPin();
                load2Screen();
            }
            else
                if (validatePin()) {
                    SharedPreferences.Editor editor = pinPref.edit();
                    editor.putString("PIN", pin);
                    editor.commit();
                    intent = new Intent(this, SetupActivity.class);
                }
                else{
                    Toast.makeText(this,"Pin number did not match, please try again",Toast.LENGTH_SHORT).show();
                    return;
                }
        }
        else if(view==backBtn) {
            if(firstScreen)
                intent = new Intent(this, InstructionActivity.class);
            else
                load1Screen();
        }
        if (intent!= null){
            startActivity(intent);
        }
    }

    private void load1Screen() {
        pin=defined=new String();
        firstPin.setText("");
        firstPin.requestFocus();
        secondPin.setText("");
        thirdPin.setText("");
        fourthPin.setText("");
        nextBtn.setText("Next");
        nextBtn.setEnabled(false);
        TextView body = findViewById(R.id.textPin);
        body.setText("Please set your PIN code and click \"Next\"");
        this.firstScreen=true;
        initPinSequence();
    }

    private void load2Screen() {
        defined=pin;
        firstPin.setText("");
        secondPin.setText("");
        thirdPin.setText("");
        fourthPin.setText("");
        nextBtn.setText("OK");
        nextBtn.setEnabled(false);
        TextView body = findViewById(R.id.textPin);
        body.setText("Please re-enter your PIN code and click \"OK\"");
        this.firstScreen=false;
        initPinSequence();
    }

    public boolean validatePin() {
        return defined.equals(pin);
    }

    private void init(){
        pin = "";
        nextBtn =  findViewById(R.id.nextBtn);
        nextBtn.setEnabled(false);
        nextBtn.setOnClickListener(this);
        firstPin =  findViewById(R.id.first_pin);
        secondPin =  findViewById(R.id.second_pin);
        thirdPin =  findViewById(R.id.third_pin);
        fourthPin = findViewById(R.id.fourth_pin);
        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
    }

    public boolean checkPin() {
        try{
            pin = firstPin.getText().toString()+
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
                    nextBtn.setEnabled(true);
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
                    nextBtn.setEnabled(true);
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
                    nextBtn.setEnabled(true);
                }
            }
        });
        fourthPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nextBtn.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (checkPin()){
                    nextBtn.setEnabled(true);
                }
            }
        });


    }
}
