package com.hey.idbyimage.idbyimage;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements  View.OnClickListener {
    private Button trybtn;
    private Button activatebtn;
    private Button settingsbtn;
    private Button removePermission;
    private DevicePolicyManager devicePolicyManager=null;
    private ComponentName componentName=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        devicePolicyManager=(DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyAdmin.class);
        setContentView(R.layout.activity_menu);
        trybtn=findViewById(R.id.trybtn);
        activatebtn=findViewById(R.id.activatebtn);
        settingsbtn=findViewById(R.id.settingsbtn);
        removePermission=findViewById(R.id.removePerm);
        trybtn.setOnClickListener(this);
        activatebtn.setOnClickListener(this);
        settingsbtn.setOnClickListener(this);
        removePermission.setOnClickListener(this);
        setupSharedPreferences();
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onClick(View v) {
        if(v==trybtn)
            startActivity(new Intent(this,LockScreenActivity.class));
        else if(v==activatebtn){
            if(!askPermission()){
                startActivity(new Intent(this, LockScreenKioskActivity.class));
                finish();
            }


        }
        else if(v==settingsbtn){
            startActivity(new Intent(this,SettingsActivity.class));
            finish();
        }

        else if(v==removePermission){
            removeAdmin();
            Toast.makeText(this, "Successfull Remove of the permission",Toast.LENGTH_SHORT).show();
        }
    }

    public void removeAdmin(){
        devicePolicyManager.removeActiveAdmin(componentName);
    }



    public boolean askPermission() {
        DevicePolicyManager devicePolicyManager=(DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(this, MyAdmin.class);
        boolean active = devicePolicyManager.isAdminActive(componentName);
        if (!active) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Need for fake lock screen");
            startActivityForResult(intent, 11);
            return true;
        }
        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        switch (requestCode){
            case 11:
                if(resultCode == Activity.RESULT_OK) {
                    startActivity(new Intent(this, LockScreenKioskActivity.class));
                    finish();
                }
                break;

        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
