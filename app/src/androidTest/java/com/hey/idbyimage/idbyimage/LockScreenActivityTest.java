package com.hey.idbyimage.idbyimage;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.locks.Lock;

import static org.junit.Assert.*;

public class LockScreenActivityTest {
    @Rule
    public ActivityTestRule<LockScreenActivity> lActivityTest = new ActivityTestRule<LockScreenActivity>(LockScreenActivity.class);

    private LockScreenActivity lActivity = null;

    ImageView[] images;
    TextView title;
    Button submit,back;

    @Before
    public void setUp() throws Exception {
        lActivity=lActivityTest.getActivity();
        title = lActivity.findViewById(R.id.titleText);
        submit = lActivity.findViewById(R.id.submit);
        back = lActivity.findViewById(R.id.backBtn);
        images = new ImageView[lActivity.getNumOfImgs()];
        for (int i=0;i<lActivity.getNumOfImgs();i++){
            int numOfImg = i+1;
            int id = lActivity.getResources().getIdentifier("img" + numOfImg, "id", lActivity.getPackageName());
            images[i] = lActivity.findViewById(id);
        }
    }

    @Test
    public void testLayout(){

        for (int i=0;i<images.length;i++)
            assertNotNull(images[i]);
        assertNotNull(back);
        assertEquals(images.length,lActivity.getNumOfImgs());
        assertNotNull(title);
        assertNotNull(submit);
    }

    @Test
    public void testSelectMore(){
        assertFalse(lActivity.isOnFailShowPin());
        try {
            lActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<=lActivity.getImgsToSelect();i++)
                        lActivityTest.getActivity().onClick(images[i]);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertFalse(lActivity.ValidateSelected());
    }

    @Test
    public void testSelectLess(){
        assertFalse(lActivity.isOnFailShowPin());
        try {
            lActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<lActivity.getImgsToSelect()-1;i++)
                        lActivityTest.getActivity().onClick(images[i]);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertFalse(lActivity.ValidateSelected());
    }


    @Test
    public void testDiversion(){
        assertFalse(lActivity.isOnFailShowPin());
        try {
            lActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<lActivity.getImgsToSelect()-1;i++)
                        lActivityTest.getActivity().onClick(images[i]);
                    lActivityTest.getActivity().onClick(submit);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertTrue(lActivity.isOnFailShowPin());
        lActivity.setOnFailShowPin(false);
    }

    @Test
    public void testDiffMatrix(){
        assertFalse(lActivity.isOnFailShowPin());
        String[] imagesName = new String[images.length];
        for(int i=0;i<imagesName.length;i++){
            imagesName[i]=(String)images[i].getTag();
        }
        try {
            lActivityTest.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<lActivity.getImgsToSelect()-1;i++)
                        lActivityTest.getActivity().onClick(images[i]);
                    lActivityTest.getActivity().onClick(submit);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        boolean ans=false;
        for(int i=0;i<imagesName.length;i++){
            if(!imagesName[i].equals((String)images[i].getTag())) {
                ans=true;
                break;
            }
        }
        assertTrue(ans);

        assertTrue(lActivity.isOnFailShowPin());
        lActivity.setOnFailShowPin(false);
    }

    @Test
    public void testSubmit(){

    }


    @After
    public void tearDown() throws Exception {
        lActivity=null;
    }

}