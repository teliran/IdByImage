package com.hey.idbyimage.idbyimage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hey.idbyimage.idbyimage.Utils.DataCollector;

import com.hey.idbyimage.idbyimage.Utils.BadRatingDistributionException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences imagePref;

    private Button back,next, helpBtn;
    private SeekBar rating1,rating2;
    private ImageView image1,image2;
    private TextView indicator,rating1prog,rating2prog;
    DataCollector dc;


    public int getNumOfImages() {
        return numOfImages;
    }

    public int getNumOfPage() {
        return numOfPage;
    }

    private int numOfImages,numOfPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        numOfImages=CountImages();
        imagePref=getSharedPreferences("imagePref", Context.MODE_PRIVATE);
        dc = DataCollector.getDataCollectorInstance();
        initElements();
        setButtonListener();
        setTextForRatings();
        loadSetupScreen();
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.BLACK);
    }

    private void setButtonListener() {
        back.setOnClickListener(this);
        next.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
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
                    Toast.makeText(SetupActivity.this,"Problem counting images",Toast.LENGTH_SHORT).show();
                }
            }
        }
        return drawables.size();
    }


    private void setTextForRatings() {
        rating1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                int dispVal=progress+1;
                rating1prog.setText("" + dispVal);
                rating1prog.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        rating2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                int dispVal=progress+1;
                rating2prog.setText("" + dispVal);
                rating2prog.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
    }

    private void initElements() {
        numOfPage=1;
        back = findViewById(R.id.backbtn);
        next = findViewById(R.id.nextbtn);
        helpBtn = findViewById(R.id.helpBtn);
        rating1=findViewById(R.id.rating1);
        rating2=findViewById(R.id.rating2);
        image1=findViewById(R.id.imagePlace1);
        image2=findViewById(R.id.imagePlace2);
        indicator=findViewById(R.id.progressIndicator);
        rating1prog=findViewById(R.id.rating1progress);
        rating2prog=findViewById(R.id.rating2progress);
        setIndicator();
    }

    @Override
    public void onClick(View v) {
        if(v==back)
            handleBackClick();
        else if(v==next)
            handleNextClick();
        else if (v==helpBtn)
            popDialog();
    }

    private void handleBackClick() {
        if(numOfPage==1) {
            saveRatings();
            startActivity(new Intent(this, InstructionActivity.class));
        }
        else{
            saveRatings();
            numOfPage--;
            loadSetupScreen();
        }

    }

    private void loadSetupScreen() {
        String imageName1=getImageFileName(2*numOfPage-1);
        String imageName2=getImageFileName(2*numOfPage);
        int drawableResourceId1 = this.getResources().getIdentifier(imageName1, "drawable", this.getPackageName());
        int drawableResourceId2 = this.getResources().getIdentifier(imageName2, "drawable", this.getPackageName());
        if(!updateRatingsFromRes(imageName1,imageName2)) {
            rating1.setProgress(0);
            rating2.setProgress(0);
            rating1prog.setText("1");
            rating2prog.setText("1");
        }
        image1.setBackgroundResource(drawableResourceId1);
        image1.setTag(imageName1);
        image2.setBackgroundResource(drawableResourceId2);
        image2.setTag(imageName2);
        if(numOfPage==numOfImages/2)
            next.setText("Done");
        else
            next.setText("Next");
        setIndicator();
    }

    private boolean updateRatingsFromRes(String imageName1, String imageName2) {
        int ratingofImage1=imagePref.getInt(imageName1,1);
        int ratingofImage2=imagePref.getInt(imageName2,1);
        if(ratingofImage1==1 && ratingofImage2==2)
            return false;
        else{
            rating1.setProgress(ratingofImage1-1);
            rating2.setProgress(ratingofImage2-1);
            return true;
        }

    }

    private void setIndicator() {
        indicator.setText(numOfPage+"/"+numOfImages/2);
    }

    private void saveRatings(){
        String imageName1 = getImageFileName(2*numOfPage-1);
        String imageName2 = getImageFileName(2*numOfPage);
        SharedPreferences.Editor editor = imagePref.edit();
        editor.putInt(imageName1, rating1.getProgress()+1);
        editor.putInt(imageName2, rating2.getProgress()+1);
        editor.commit();
        String msg = "Just retrived " + imagePref.getInt(imageName1,0) + "and" +imagePref.getInt(imageName2,0);
    }

    private String getImageFileName(int index){
        return "p"+index;
    }

    private void handleNextClick() {
        if (numOfPage == numOfImages / 2) {
            saveRatings();

            ShuffleAlgorithm algo = new ShuffleAlgorithm(getAllRatingsMap());
            try {
                    algo.shuffle(2, 7);
                    algo.shuffle(4, 5);
            }catch (BadRatingDistributionException e){
                Toast.makeText(this,"Rating need to be spread",Toast.LENGTH_LONG).show();
                return;
            }

            //-----------Data Collecting---------------
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
            //-----------End of Data Collecting------------

            startActivity(new Intent(this,MenuActivity.class));
            finish();
        }
        else{
            saveRatings();
            numOfPage++;
            loadSetupScreen();
        }
    }

    /*public HashMap<String, Integer> getAllRatingsMap(){
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
    }*/

    private void popDialog(){
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Instructions");
        ad.setMessage("These are some instructions");
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        //ad.create();
        ad.show();
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

}
