package com.hey.idbyimage.idbyimage;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetupActivityTest {
    @Rule
    public ActivityTestRule<SetupActivity> sActivityTest = new ActivityTestRule<SetupActivity>(SetupActivity.class);

    private SetupActivity sActivity = null;
    private Button back;
    private Button next;
    private SeekBar rating1,rating2;
    private ImageView image1,image2;
    private TextView indicator,rating1prog,rating2prog;

    @Before
    public void setUp() throws Exception {
        sActivity = sActivityTest.getActivity();
         back= sActivity.findViewById(R.id.backbtn);
         next=sActivity.findViewById(R.id.nextbtn);
         rating1=sActivity.findViewById(R.id.rating1);
         rating2=sActivity.findViewById(R.id.rating2);
         image1=sActivity.findViewById(R.id.imagePlace1);
         image2=sActivity.findViewById(R.id.imagePlace2);
         indicator=sActivity.findViewById(R.id.progressIndicator);
         rating1prog=sActivity.findViewById(R.id.rating1progress);
         rating2prog=sActivity.findViewById(R.id.rating2progress);
    }


    @Test
    public void testLayout(){
        Button back= sActivity.findViewById(R.id.backbtn);
        Button next=sActivity.findViewById(R.id.nextbtn);
        SeekBar rating1=sActivity.findViewById(R.id.rating1),rating2=sActivity.findViewById(R.id.rating2);
        ImageView image1=sActivity.findViewById(R.id.imagePlace1),image2=sActivity.findViewById(R.id.imagePlace2);
        TextView indicator=sActivity.findViewById(R.id.progressIndicator),rating1prog=sActivity.findViewById(R.id.rating1progress),rating2prog=sActivity.findViewById(R.id.rating2progress);
        assertNotNull(back);
        assertNotNull(next);
        assertNotNull(rating1);
        assertNotNull(rating2);
        assertNotNull(image1);
        assertNotNull(image2);
        assertNotNull(indicator);
        assertNotNull(rating1prog);
        assertNotNull(rating2prog);
    }

    @Test
    public void testNext(){
        int numOfPage=sActivity.getNumOfPage();
        int numOfImg = sActivity.getNumOfImages();
        int pages = numOfImg/2;
        assertEquals(numOfPage,1);
        int prog1 = rating1.getProgress()+1;
        int prog2 = rating2.getProgress()+1;
        assertEquals(rating1prog.getText(),""+prog1);
        assertEquals(rating2prog.getText(),""+prog2);
        assertEquals(indicator.getText(), numOfPage+"/"+pages);
        assertEquals(image1.getTag(),"p1");
        assertEquals(image2.getTag(),"p2");

        //simulate a click, only the ui thread can raise this event (Modifing the views
        try {
            sActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sActivityTest.getActivity().onClick(next);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
    }


        int numOfPage2=sActivity.getNumOfPage();
        numOfImg = sActivity.getNumOfImages();
        pages = numOfImg/2;
        assertEquals(numOfPage2,2);
        prog1 = rating1.getProgress()+1;
        prog2 = rating2.getProgress()+1;
        assertEquals(rating1prog.getText(),""+prog1);
        assertEquals(rating2prog.getText(),""+prog2);
        assertEquals(indicator.getText(), numOfPage2+"/"+pages);
        assertEquals(image1.getTag(),"p3");
        assertEquals(image2.getTag(),"p4");
        assertEquals(numOfPage+1,numOfPage2);

        //Return to original state
        try {
            sActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sActivityTest.getActivity().onClick(back);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    @Test
    public void testBack(){
        //simulate a click, only the ui thread can raise this event (Modifing the views
        int prog1 = rating1.getProgress();
        int prog2 = rating2.getProgress();
        String indic = indicator.getText().toString();
        String prog1String = rating1prog.getText().toString();
        String prog2String = rating2prog.getText().toString();

        assertEquals(image1.getTag(),"p1");
        assertEquals(image2.getTag(),"p2");
        try {
            sActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sActivityTest.getActivity().onClick(next);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //simulate a click, only the ui thread can raise this event (Modifing the views
        try {
            sActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sActivityTest.getActivity().onClick(back);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertEquals(image1.getTag(),"p1");
        assertEquals(image2.getTag(),"p2");
        assertEquals(prog1, rating1.getProgress());
        assertEquals(prog2, rating2.getProgress());
        assertEquals(indic,indicator.getText().toString());
        assertEquals(prog1String,rating1prog.getText().toString());
        assertEquals(prog2String,rating2prog.getText().toString());
    }
    @After
    public void tearDown() throws Exception {
        sActivity = null;
    }
}