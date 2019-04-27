package com.hey.idbyimage.idbyimage;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.IDNA;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.InflaterOutputStream;

public class LockScreenActivity extends AppCompatActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences imagePref;
    private Button submit,back;
    private ArrayList<String> selected;
    private TextView screenIndic;
    private boolean onFailShowPin;
    private int numOfImgs;
    private int imgsToSelect;
    private int numOfScreens;
    private int currentScreenNum;

    private ShuffleAlgorithm shuffleAlgorithm;
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setupSharedPreferences(sharedPreferences);
        loadMatrixSizeFromPreference(sharedPreferences);
        loadNumOfimagesFromPreference(sharedPreferences);
        loadNumOfScreensFromPreference(sharedPreferences);
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

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.BLACK);
    }

    private void setupSharedPreferences(SharedPreferences sharedPreferences) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void initVars() {
        imagePref=getSharedPreferences("imagePref", Context.MODE_PRIVATE);
        selected=new ArrayList<String>();
        shuffleAlgorithm=new ShuffleAlgorithm(getAllRatingsMap());
        onFailShowPin=false;
        currentScreenNum=1;
        updateImages();

        TextView titleText = findViewById(R.id.titleText);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(this);
        back=findViewById(R.id.backBtn);
        back.setOnClickListener(this);
        titleText.setText("Select "+this.imgsToSelect+" Images");
        screenIndic=findViewById(R.id.ScreenNumIndic);
        screenIndic.setText(currentScreenNum+"/"+numOfScreens);
    }

    private void updateImages() {
        List<Map.Entry<String, Integer>> imagesEntry = shuffleAlgorithm.shuffle(imgsToSelect,numOfImgs-imgsToSelect);
        ArrayList<String> images = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry:imagesEntry) {
            images.add(entry.getKey());
        }
        updateImageView(images);
    }

    private void updateImageView(ArrayList<String> images) {
        for (int i = 1; i <= images.size(); i++) {
            int drawableResourceId = this.getResources().getIdentifier(images.get(i-1), "drawable", this.getPackageName());
            int id = getResources().getIdentifier("img" +i, "id", this.getPackageName());
            ImageView img = findViewById(id);
            img.setImageResource(drawableResourceId);
            img.setTag(images.get(i-1));
        }
    }

    private Bitmap addWaterMark(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.checked);
        //  canvas.drawBitmap(waterMark, 0, 0, null);
        int startX= (canvas.getWidth()-waterMark.getWidth())/2;//for horisontal position
        int startY=(canvas.getHeight()-waterMark.getHeight())/2;//for vertical position
        canvas.drawBitmap(waterMark,startX,startY,null);

        return result;
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
                try {
                    drawables.add(field.getInt(null));
                } catch (IllegalAccessException e) {
                    Toast.makeText(this,"Problem counting images",Toast.LENGTH_SHORT).show();
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
        else if(v==back)
            finish();
        else {
            int id=v.getId();
            ImageView img =findViewById(id);
            String name = (String) img.getTag();
            int imgID = this.getResources().getIdentifier(name, "drawable", this.getPackageName());
            if (selected.contains(name)) {
                selected.remove(name);
                img.setImageResource(imgID);
                return;
            }
            int drawableResourceId = this.getResources().getIdentifier("checked", "drawable", this.getPackageName());
            img.setImageDrawable(this.getResources().getDrawable(drawableResourceId,null));
            selected.add(name);
        }
    }

    private void handleSubmit() {
        if(ValidateSelected()) {
            if (currentScreenNum == numOfScreens){
                Toast.makeText(this, "Success & finished", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                selected = new ArrayList<String>();
                currentScreenNum++;
                screenIndic.setText(currentScreenNum+"/"+numOfScreens);
                updateImages();
                onFailShowPin=false;
                Toast.makeText(this,"Success but not finished",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if (!onFailShowPin) {
                selected = new ArrayList<String>();
                updateImages();
                onFailShowPin=true;
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
            }
            else{
                Intent pinLock = new Intent(this,PinLockScreenActivity.class);
                startActivity(pinLock);
                finish();
            }
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
}
