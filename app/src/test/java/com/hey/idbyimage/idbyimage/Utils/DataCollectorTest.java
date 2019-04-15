package com.hey.idbyimage.idbyimage.Utils;


import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class DataCollectorTest {
    DataCollector dc = DataCollector.getDataCollectorInstance();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCheckConnectivity(){
        try {
            assertTrue(dc.getServerStatus());
        }catch(Exception e){
            //System.out.println(e.getMessage());
            fail(e.getMessage());
        }
    }

    @Test
    public void testSendUserRatingDataToServer(){

        int randUser = (int)(Math.random()*10000000);
        int randRating = (int)(Math.random()*10)+1;
        int randImage = (int)(Math.random()*50)+1;
        JSONObject userImageRating = new JSONObject();

        try {
            userImageRating.put("userId", "TEST_USER_"+randUser);
            userImageRating.put("imageId", "p"+randImage);
            userImageRating.put("rating", randRating+"");
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        DataCollector dc = DataCollector.getDataCollectorInstance();
        boolean ans = dc.sendDataToServer(userImageRating, "/ratings");
        assertTrue(ans);
    }


    @Test
    public void testSendAllUserRatingsToServer() {
        HashMap<String, Integer> allImages = new HashMap<>();
        for (int i=1; i<51; i++){
            allImages.put("p"+i,(int)(Math.random()*10)+1);
        }
        DataCollector dc = DataCollector.getDataCollectorInstance();
        dc.setImageRatingsData(allImages);
        int randUser = (int)(Math.random()*10000000);
        boolean ans = dc.sendAllUserRatingsToServer("TestAllRatingsUser_"+randUser);
        assertTrue(ans);
    }

    @Test
    public void testSendUserDataToServer() {
        String randUser = "UserDataTest_" + (int)(Math.random()*10000000);
        String randAge = (int)(Math.random()*100)+15 + "";
        DataCollector dc = DataCollector.getDataCollectorInstance();
        boolean ans = dc.sendUserDataToServer(randUser, randAge, "TEST_GENDER");
        assertTrue(ans);
    }

   /* @Test
    public void testGenerateUniqueID() {
        class TestActivity extends AppCompatActivity {
        }
        TestActivity testClass = new TestActivity();

        DataCollector dc = DataCollector.getDataCollectorInstance();
        String id = dc.id(testClass);
        System.out.println(id);
        assertTrue(true);
    }*/
}