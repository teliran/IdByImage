package com.hey.idbyimage.idbyimage;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hey.idbyimage.idbyimage.Utils.ActionObject;
import com.hey.idbyimage.idbyimage.Utils.BadRatingDistributionException;
import com.hey.idbyimage.idbyimage.Utils.BaseActivity;
import com.hey.idbyimage.idbyimage.Utils.DataCollector;
import com.hey.idbyimage.idbyimage.Utils.KioskMode;
import com.hey.idbyimage.idbyimage.Utils.MySharedPreferences;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LockScreenKioskActivity extends BaseActivity implements View.OnClickListener,SharedPreferences.OnSharedPreferenceChangeListener{
    private SharedPreferences imagePref;
    private SharedPreferences wasImagesSentFlag;
    private Button submit,back;
    private ArrayList<String> selected;
    private boolean onFailShowPin;
    private int numOfImgs;
    private int imgsToSelect;
    private int numOfScreens;
    private int currentScreenNum;
    private TextView screenIndic;

    private ShuffleAlgorithm shuffleAlgorithm;
    //--------------------Data collecting -----------------------
    private DataCollector dc = DataCollector.getDataCollectorInstance();
    //private final  String sessionId = dc.generateUniqueSessionId(); //move
    private ActionObject actionsData; //Data Object for storing the actions data
    private long timeStart;
    private long timeEnd;
    //----------------------------------------------------------------------
    private static final String TAG = LockScreenKioskActivity.class.getSimpleName();
    final private FragmentManager fragmentManager = getSupportFragmentManager();
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName componentName;

    //Field for ImageSelectionAlgo - api: createImgSet:HashMap<String,Integer>->ArrayList<String>, getMean: ->float, getDev: ()->float


    public SharedPreferences getImagePref() {
        return imagePref;
    }

    public Button getSubmit() {
        return submit;
    }

    public ArrayList<String> getSelected() {
        return selected;
    }

    public boolean isOnFailShowPin() {
        return onFailShowPin;
    }

    public int getNumOfImgs() {
        return numOfImgs;
    }

    public int getImgsToSelect() {
        return imgsToSelect;
    }

    public void setOnFailShowPin(boolean onFailShowPin) {
        this.onFailShowPin = onFailShowPin;
    }

    public void setSelected(ArrayList<String> selected) {
        this.selected = selected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionsData = new ActionObject();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setupSharedPreferences(sharedPreferences);
        loadMatrixSizeFromPreference(sharedPreferences);
        loadNumOfimagesFromPreference(sharedPreferences);
        loadNumOfScreensFromPreference(sharedPreferences);
        actionsData.setUserId(dc.id(this));
        actionsData.setTotalScreens(numOfScreens);
        if(numOfImgs==9)
            setContentView(R.layout.activity_lock_screen_3x3);
        else
            setContentView(R.layout.activity_lock_screen_3x2);
        initVars();

        for (int i=0;i<numOfImgs;i++){
            int numOfImg = i+1;
            int id = getResources().getIdentifier("img" + numOfImg, "id", this.getPackageName());
            ImageView img = findViewById(id);
            img.setOnClickListener(this);
        }
        submit.setEnabled(false);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.BLACK);
        sendAllImagesRatings(); //This is in case the ratings were not send in setup.
        devicePolicyManager=(DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyAdmin.class);
        setUpKioskMode();
        devicePolicyManager.lockNow();
    }

    private void setupSharedPreferences(SharedPreferences sharedPreferences) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void initVars() {
        imagePref=getSharedPreferences("imagePref", Context.MODE_PRIVATE);
        wasImagesSentFlag = getSharedPreferences("wasImagesSentFlag", Context.MODE_PRIVATE);
        selected=new ArrayList<String>();
        shuffleAlgorithm=new ShuffleAlgorithm(getAllRatingsMap());
        onFailShowPin=false;
        currentScreenNum=1;
        updateImages();

        TextView titleText = findViewById(R.id.titleText);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(this);
        back=findViewById(R.id.backBtn);
        back.setVisibility(View.INVISIBLE);
        String headerText = "Select "+this.imgsToSelect+" Images";
        titleText.setText(headerText);
        screenIndic=findViewById(R.id.ScreenNumIndic);
        screenIndic.setText(currentScreenNum+"/"+numOfScreens);
    }

    private void updateImages() {
        try {
            List<Map.Entry<String, Integer>> imagesEntry = shuffleAlgorithm.shuffle(imgsToSelect, numOfImgs - imgsToSelect);
            ArrayList<String> images = new ArrayList<String>();
            for (Map.Entry<String, Integer> entry:imagesEntry) {
                images.add(entry.getKey());
            }
            actionsData.setShown(images);
            actionsData.setTopRated(shuffleAlgorithm.getCorrectAnswer());
            updateImageView(images);
        }catch (BadRatingDistributionException e){
            Toast.makeText(this,"This should not happen",Toast.LENGTH_SHORT).show();
        }
    }


    private void updateImageView(ArrayList<String> images) {
        for (int i = 1; i <= images.size(); i++) {
            int drawableResourceId = this.getResources().getIdentifier(images.get(i-1), "drawable", this.getPackageName());
            int id = getResources().getIdentifier("img" +i, "id", this.getPackageName());
            ImageView img = findViewById(id);
            img.setBackgroundResource(drawableResourceId);
            img.setTag(images.get(i-1));
            img.setImageResource(android.R.color.transparent);
        }
    }

    public HashMap<String, Integer> getAllRatingsMap(){
        HashMap<String,Integer> imageRatings = new HashMap<String, Integer>();
        int loopIndex = CountImages();
        for (int i=1;i<=loopIndex;i++){
            String imgName=getImageFileName(i);
            int ratingofImage=imagePref.getInt(imgName,0);
            if(ratingofImage>0)
                imageRatings.put(imgName,ratingofImage);
            else
                break;
        }
        return imageRatings;
    }

    private int CountImages() {
        Field[] fields = R.drawable.class.getFields();
        List<Integer> drawables = new ArrayList<Integer>();
        for (Field field : fields) {
            // Take only those with name starting with "p"
            if (field.getName().startsWith("p")) {
                String string = field.getName().substring(1,field.getName().length());
                try {
                    Double num = Double.parseDouble(string);
                    drawables.add(field.getInt(null));

                } catch (IllegalAccessException e) {
                    Toast.makeText(this,"Problem counting images",Toast.LENGTH_SHORT).show();
                }
                catch (NumberFormatException e){

                }
            }
        }
        return drawables.size();
    }

    private String getImageFileName(int index){
        return "p"+index;
    }

    @Override
    public void onClick(View v) {
        if(v==submit){
            handleSubmit();
        }
        else {
            int id=v.getId();
            ImageView img =findViewById(id);
            String name = (String) img.getTag();
            if (selected.contains(name)) {
                selected.remove(name);
                img.setImageResource(android.R.color.transparent);
                return;
            }
            int drawableResourceId = this.getResources().getIdentifier("checked", "drawable", this.getPackageName());
            img.setImageDrawable(this.getResources().getDrawable(drawableResourceId,null));
            selected.add(name);
            if(selected.size()==imgsToSelect){
                submit.setEnabled(true);
            }
        }
    }

    private void handleSubmit() {
        if(ValidateSelected()){
            //session id should here
            if (currentScreenNum == numOfScreens){
                kioskMode.lockUnlock(this, false);
                actionsData.setTimeStamp(dc.getCurrentTimestamp());
                actionsData.setSelected(selected);
                actionsData.setSuccess(true);
                timeEnd = System.currentTimeMillis();
                actionsData.setScreenOrder(currentScreenNum);
                actionsData.setTimeToPass((int)(timeEnd-timeStart));
                this.finish();
                runQueryThread();
                moveTaskToBack(true);
                System.exit(0);
            }
            else
            {
                selected = new ArrayList<String>();
                actionsData.setScreenOrder(currentScreenNum);
                currentScreenNum++;
                updateImages();
                actionsData.setSelected(selected);
                actionsData.setSuccess(true);
                timeEnd = System.currentTimeMillis();
                actionsData.setTimeToPass((int)(timeEnd-timeStart));
                actionsData.setTimeStamp(dc.getCurrentTimestamp());
                screenIndic.setText(currentScreenNum+"/"+numOfScreens);
                runQueryThread();
            }


        }
        else {//
            actionsData.setSuccess(false);
            actionsData.setTimeStamp(dc.getCurrentTimestamp());
            timeEnd = System.currentTimeMillis();
            actionsData.setScreenOrder(currentScreenNum);
            if (!onFailShowPin) {
                actionsData.setSelected(selected);
                actionsData.setTimeToPass((int)(timeEnd-timeStart));
                selected = new ArrayList<String>();
                updateImages();
                onFailShowPin=true;
                runQueryThread();
            }
            else{
                actionsData.setSelected(selected);
                actionsData.setTimeToPass((int)(timeEnd-timeStart));
                actionsData.setTimeStamp(dc.getCurrentTimestamp());
                //runQueryThread();
                Intent pinLock = new Intent(this,PinLockScreenActivity.class);
                startActivity(pinLock);
                finish();
                runQueryThread();
            }

        }
    }

    private void runQueryThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                dc.sendActionsDataToServer(actionsData);
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        try {
            Thread.currentThread().join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean ValidateSelected() {
        if(selected.size()!=imgsToSelect) //Checking for valid number of selected imgs
            return false;
        double avg = shuffleAlgorithm.getAvg();
        double dev = shuffleAlgorithm.getDev();
        for (int i=0;i<imgsToSelect;i++){
            if(imagePref.getInt(selected.get(i),0)<=0 || imagePref.getInt(selected.get(i),0)<avg+dev) //Checking that the user ranked them higher then mean+dev
                return false;
        }
        return true;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("matrix_size")) {
            Log.d("MATRIX_SIZE",sharedPreferences.getString(getString(R.string.Number_of_images_screen),getString(R.string.pref_num_images_3x3)));
            loadMatrixSizeFromPreference(sharedPreferences);
        }
        else if (key.equals("Number_of_images")){
            loadNumOfimagesFromPreference(sharedPreferences);
        }
        else if (key.equals("Num_lock_screens")){
            loadNumOfScreensFromPreference(sharedPreferences);
        }
    }

    private void loadMatrixSizeFromPreference(SharedPreferences sharedPreferences) {
        try {
            this.numOfImgs = Integer.parseInt(sharedPreferences.getString(getString(R.string.Number_of_images_screen),getString(R.string.pref_num_images_3x3)));
            Log.d("MATRIX_SIZE",sharedPreferences.getString(getString(R.string.Number_of_images_screen),getString(R.string.pref_num_images_3x3)));
        } catch(NumberFormatException nfe){
            Log.e("Cannot Parse String","MATRIX_SIZE");
        };
    }

    private void loadNumOfimagesFromPreference(SharedPreferences sharedPreferences) {
        try {
            this.imgsToSelect = Integer.parseInt(sharedPreferences.getString(getString(R.string.Number_selected_images),getString(R.string.pref_num_images_3)));
            Log.d("Images_To_Select",sharedPreferences.getString(getString(R.string.Number_selected_images),getString(R.string.pref_num_images_3)));
        } catch(NumberFormatException nfe){
            Log.e("Cannot Parse String","Images_To_Select");
        };
    }

    private void loadNumOfScreensFromPreference(SharedPreferences sharedPreferences) {
        try {
            this.numOfScreens = Integer.parseInt(sharedPreferences.getString(getString(R.string.Num_of_lock_screens),getString(R.string.pref_num_of_screens_1)));
            Log.d("Num_Of_Screens", sharedPreferences.getString(getString(R.string.Num_of_lock_screens),getString(R.string.pref_num_of_screens_1)));
        } catch(NumberFormatException nfe){
            Log.e("Cannot Parse String","Num_Of_Screens");
        };
    }


    private void setUpKioskMode() {
        if (!MySharedPreferences.isAppLaunched(this)) {
            Log.d(TAG, "onCreate() locking the app first time");
            kioskMode.lockUnlock(this, true);
            MySharedPreferences.saveAppLaunched(this, true);
        } else {
            //check if app was locked
            if (MySharedPreferences.isAppInKioskMode(this)) {
                Log.d(TAG, "onCreate() locking the app");
                kioskMode.lockUnlock(this, true);
            }
            kioskMode.lockUnlock(this,true);
        }
    }

    public void askPermission() {
        boolean active = devicePolicyManager.isAdminActive(componentName);
        if (!active) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Need for fake lock screen");
            startActivityForResult(intent, RESULT_ENABLE);
        }

    }


    @Override
    public void onResume(){
        super.onResume();
        if (!onFailShowPin)
            timeStart = System.currentTimeMillis();// start from the moment pics are shown
        final  String sessionId = dc.generateUniqueSessionId();
        actionsData.setSessionId(sessionId);
        selected = new ArrayList<String>();
        currentScreenNum=1;
        onFailShowPin=false;
        updateImages();
        screenIndic.setText(currentScreenNum+"/"+numOfScreens);
    }

    @Override
    public void onBackPressed() {
        if (!kioskMode.isLocked(this)) {
            super.onBackPressed();
        }
    }

    private boolean getValueFromWasSentPref(){
       return wasImagesSentFlag.getBoolean("Was_Sent", false);
    }

    private void setWasImagesSentFlag(boolean wasSent){
        SharedPreferences.Editor editor = wasImagesSentFlag.edit();
        editor.putBoolean("Was_Sent", wasSent);
        editor.commit();
    }

    private void sendAllImagesRatings(){
        if (getValueFromWasSentPref()){
            return;
        }
        dc.checkServerStatus();
        if (dc.getServerStatus()==200) {
            setWasImagesSentFlag(true);
            dc.setImageRatingsData(getAllRatingsMap());
            final String id = dc.id(this);
            Log.i("UNIQUE ID: ", id);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    dc.sendAllUserRatingsToServer(id);
                }
            });
            t.setPriority(Thread.MAX_PRIORITY);
            t.start();
            //dc.sendAllUserRatingsToServer(id);
            try {
                Thread.currentThread().join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}

