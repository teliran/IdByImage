package com.hey.idbyimage.idbyimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hey.idbyimage.idbyimage.ControllerClass.SetupController;
import com.hey.idbyimage.idbyimage.ControllerClass.SetupControllerImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    Button back,next;
    SeekBar rating1,rating2;
    ImageView image1,image2;
    TextView indicator,rating1prog,rating2prog;
    int numOfImages,numOfPage;
    SetupController control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        initElements();
        setButtonListener();
        setTextForRatings();
        loadSetupScreen();
        numOfImages=CountImages();
    }

    private void setButtonListener() {
        back.setOnClickListener(this);
        next.setOnClickListener(this);
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
    }

    private void handleBackClick() {
        if(numOfPage==1)
            //move to the prevActivity
            return;
        else{
            numOfPage--;
            loadSetupScreen();
        }

    }

    private void loadSetupScreen() {
        int drawableResourceId1 = this.getResources().getIdentifier(getImageFileName(2*numOfPage-1), "drawable", this.getPackageName());
        int drawableResourceId2 = this.getResources().getIdentifier(getImageFileName(2*numOfPage), "drawable", this.getPackageName());
        rating1.setProgress(0);
        rating2.setProgress(0);
        image1.setBackgroundResource(drawableResourceId1);
        image2.setBackgroundResource(drawableResourceId2);
        rating1prog.setText("0");
        rating2prog.setText("0");
        setIndicator();
    }

    private void setIndicator() {
        indicator.setText(numOfPage+"/"+numOfImages/2);
    }


    private String getImageFileName(int index){
        return "p"+index;
    }

    private void handleNextClick() {
        if(numOfPage==numOfImages/2)
            //move to the next screen - finished rating
            return;
        else
            numOfPage++;
            loadSetupScreen();
    }
}
