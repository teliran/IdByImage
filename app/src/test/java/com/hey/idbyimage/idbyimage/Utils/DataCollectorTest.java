package com.hey.idbyimage.idbyimage.Utils;


import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.hey.idbyimage.idbyimage.ShuffleAlgorithm;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class DataCollectorTest {
    DataCollector dc;
    @Before
    public void setUp() throws Exception {
        dc = DataCollector.getDataCollectorInstance();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCheckConnectivity(){
        try {
            int status = dc.getServerStatus();
            System.out.println(status);
            assertTrue(status == 200);
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
        //DataCollector dc = DataCollector.getDataCollectorInstance();
        boolean ans = dc.sendDataToServer(userImageRating, "/ratings");
        assertTrue(ans);
    }


    @Test
    public void testSendAllUserRatingsToServer() {
        HashMap<String, Integer> allImages = new HashMap<>();
        for (int i=1; i<51; i++){
            allImages.put("p"+i,(int)(Math.random()*10)+1);
        }
        //DataCollector dc = DataCollector.getDataCollectorInstance();
        dc.setImageRatingsData(allImages);
        int randUser = (int)(Math.random()*10000000);
        boolean ans = dc.sendAllUserRatingsToServer("TestAllRatingsUser_"+randUser);
        assertTrue(ans);
    }

    @Test
    public void testSendUserDataToServer() {
        String randUser = "UserDataTest_" + (int)(Math.random()*10000000);
        String randAge = (int)(Math.random()*100)+15 + "";
        //DataCollector dc = DataCollector.getDataCollectorInstance();
        boolean ans = dc.sendUserDataToServer(randUser, randAge, "TEST_GENDER");
        assertTrue(ans);
    }

    @Test
    public void generateUniqueSessionId() {
        HashSet<String> ids = new HashSet<>();
        for (int i = 0; i<2000; i++){
            String sessionId = dc.generateUniqueSessionId();
            if (ids.contains(sessionId)){
                fail("SessionID should be unique");
            }
            ids.add(sessionId);
        }
        assertTrue(true);
    }
    //SessionID should be a String
    @Test
    public void sendActionsDataToServer() {
        ActionObject actions = createActionObject();
        actions.printActionObject();
        //assertTrue(true);
        assertTrue(dc.sendActionsDataToServer(actions));
        //actions.printActionObject();

    }

    private ActionObject createActionObject(){
        HashMap<String, Integer> allImages = new HashMap<>();
        for (int i=1; i<51; i++){
            allImages.put("p"+i,(int)(Math.random()*10)+1);
        }
        ShuffleAlgorithm alg = new ShuffleAlgorithm(allImages);
        List<Map.Entry<String, Integer>> l = null;
        try {
            l = alg.shuffle(3, 6);
        } catch (BadRatingDistributionException e) {
            fail();
        }
        int randUser = (int)(Math.random()*10000000);
        ActionObject actions = new ActionObject();
        actions.setScreenOrder(1);
        actions.setTotalScreens(3);
        actions.setSelected(getListFromMapEntry(alg.getCorrectAnswer()));
        actions.setTopRated(alg.getCorrectAnswer());
        actions.setShown(getListFromMapEntry(l));
        actions.setSuccess(true);
        actions.setSessionId("TestSession_"+randUser);
        actions.setUserId("TestUser_"+randUser);
        actions.setTimeStamp(dc.getCurrentTimestamp());
        actions.setTimeToPass(0002455);
        return actions;
    }

    private List<String> getListFromMapEntry(List<Map.Entry<String, Integer>> l){
        ArrayList<String> retList = new ArrayList<>();
        for (Map.Entry<String, Integer> e: l){
            retList.add(e.getKey());
        }
        return retList;
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