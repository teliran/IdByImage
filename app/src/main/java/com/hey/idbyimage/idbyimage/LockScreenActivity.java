package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LockScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences imagePref;
    private Button submit,back;
    private ArrayList<String> selected;
    private boolean onFailShowPin;
    private int numOfImgs;
    private int imgsToSelect = 3;
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
        SharedPreferences settingsPref=getSharedPreferences("settingsPref", Context.MODE_PRIVATE);
        int a =settingsPref.getInt("numOfImagesToShow",0);
        int b =settingsPref.getInt("numOfImagesToSelect",0);
        Toast.makeText(this,"To show: "+a+" To Select: "+b,Toast.LENGTH_SHORT).show();
        //Check matrix scale- if 3x2:
        //numOfImgs=6;
        //this.imgsToSelect=2
        //setContentView(R.layout.activity_lock_screen_3x2);
        //else
        setContentView(R.layout.activity_lock_screen_3x3);
        initVars();

        for (int i=0;i<numOfImgs;i++){
            int numOfImg = i+1;
            int id = getResources().getIdentifier("img" + numOfImg, "id", this.getPackageName());
            ImageView img = findViewById(id);
            img.setOnClickListener(this);
        }
    }

    private void initVars() {
        imagePref=getSharedPreferences("imagePref", Context.MODE_PRIVATE);
        selected=new ArrayList<String>();
        shuffleAlgorithm=new ShuffleAlgorithm(getAllRatingsMap());
        onFailShowPin=false;
        numOfImgs=9;
        updateImages();

        submit=findViewById(R.id.submit);
        submit.setOnClickListener(this);
        back=findViewById(R.id.backBtn);
        back.setOnClickListener(this);
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
            startActivity(new Intent(this,MenuActivity.class));
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
        }
    }

    private void handleSubmit() {
        if(ValidateSelected()){
            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
        }
        else {
            if (!onFailShowPin) {
                selected = new ArrayList<String>();
                updateImages();
                onFailShowPin=true;
            }
            else{
                //Move to pin lock screen
                //TODO
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

}
