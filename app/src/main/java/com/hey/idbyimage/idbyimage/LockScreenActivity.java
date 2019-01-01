package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LockScreenActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences imagePref;
    Button submit;
    ArrayList<String> selected;
    boolean onFailShowPin;
    int numOfImgs;
    int imgsToSelect = 3;
    //Field for ImageSelectionAlgo - api: createImgSet:HashMap<String,Integer>->ArrayList<String>, getMean: ->float, getDev: ()->float

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        imagePref=getSharedPreferences("imagePref", Context.MODE_PRIVATE);
        selected=new ArrayList<String>();
        onFailShowPin=false;
        updateImages();
        numOfImgs=12;
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(this);
        for (int i=0;i<numOfImgs;i++){
            int numOfImg = i+1;
            int id = getResources().getIdentifier("R.id.img" + numOfImg, "id", this.getPackageName());
            ImageView img = findViewById(id);
            img.setOnClickListener(this);
        }
    }

    private void updateImages() {
        HashMap<String,Integer> imageRatings = getAllRatingsMap();
        ArrayList<String> images = ImageSelectionAlgo.createImgSet(imageRatings);//Call the shuffle algorthem
        updateImageView(images);
    }

    private void updateImageView(ArrayList<String> images) {
        for (int i = 1; i <= images.size(); i++) {
            int drawableResourceId = this.getResources().getIdentifier(images.get(i-1), "drawable", this.getPackageName());
            int id = getResources().getIdentifier("R.id.img" + i, "id", this.getPackageName());
            ImageView img = findViewById(id);
            img.setBackgroundResource(drawableResourceId);
            img.setTag(images.get(i-1));
        }
    }

    private HashMap<String, Integer> getAllRatingsMap(){
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
        else {
            int id=v.getId();
            ImageView img =findViewById(id);
            String name = (String) img.getTag();
            if (selected.contains(name)) {
                selected.remove(name);
                //Remove sign from img
                //TODO
            }

            //TODO
            //Add the sign to show user that that photo has been selected
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

    private boolean ValidateSelected() {
        if(selected.size()!=imgsToSelect) //Checking for valid number of selected imgs
            return false;
        float avg = ImageSelectionAlgo.getMean();
        float dev = ImageSelectionAlgo.getDev();
        for (int i=0;i<imgsToSelect;i++){
            if(imagePref.getInt(selected.get(i),0)<=0 || imagePref.getInt(selected.get(i),0)<avg+dev) //Checking that the user ranked them higher then mean+dev
                return false;
        }
        return true;
    }
}
