package com.hey.idbyimage.idbyimage.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;


import java.io.DataOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * DataCollector - Single Object intended to collect the user's data,
 * Organize it and send it to the server.
 */
public class DataCollector {

    private static  final String SERVER_URL = "http://132.72.23.63:3001";
    private static DataCollector dataCollector = null;
    private int serverStatus;
    private boolean queryResp;
    private HashMap<String, Integer> allImagesRatings;
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    private DataCollector(){
        checkServerStatus();
        try {
            Thread.currentThread().join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.allImagesRatings = new HashMap<>();

    }

    public static DataCollector getDataCollectorInstance(){
        if (dataCollector == null){
            dataCollector = new DataCollector();
        }
        return  dataCollector;
    }

    public void checkServerStatus(){
        //Create a URL object holding our url : SERVER_URL+"/healthcheck"
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL myUrl = new URL(SERVER_URL+"/healthcheck");
                    //Create a connection
                    HttpURLConnection connection = (HttpURLConnection)
                            myUrl.openConnection();
                    //Set methods and timeouts
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(15000);
                    connection.setConnectTimeout(15000);

                    //Connect to our url
                    connection.connect();
                    serverStatus = connection.getResponseCode();
                }catch(Exception e){
                    Log.e("ERROR", e.getMessage());
                }
            }
        });
        thread.setName("Check connectivity");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

    }

    public int getServerStatus(){
        return this.serverStatus;
    }

    public void setImageRatingsData(HashMap<String, Integer> map){
        this.allImagesRatings = map;
    }

    public boolean sendAllUserRatingsToServer(String userID){
        if (serverStatus!=200){
            return false;
        }
        for (Map.Entry<String, Integer> imageRatingEntry: allImagesRatings.entrySet()){
            JSONObject userImageRating = getImageRatingJsonObject(userID, imageRatingEntry.getKey(), imageRatingEntry.getValue());
            if(!sendDataToServer(userImageRating, "/ratings")){
                return false;
            }
        }
        return true;
    }

    public boolean sendUserDataToServer(String userID, String age, String gender){
        if (serverStatus!=200){
            return false;
        }
        JSONObject userJson = getUserJsonObject(userID, age, gender);
        if (!sendDataToServer(userJson, "/users")){
            return false;
        }
        return true;
    }

    public boolean sendDataToServer(final JSONObject jsonObj, final String urlPrefix){
        if (serverStatus!=200){
            return false;
        }
       // Thread thread = new Thread(new Runnable() {
          //  @Override
          //  public void run() {
                String url = SERVER_URL+urlPrefix;
                try {
                    URL myUrl = new URL(url);
                    //DataInputStream input;
                    HttpURLConnection connection = (HttpURLConnection)
                            myUrl.openConnection();
                    connection.setConnectTimeout(15000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.connect();
                    //Log.i("JSON", jsonObj.toString());
                    DataOutputStream printout = new DataOutputStream((connection.getOutputStream()));
                    printout.writeBytes(jsonObj.toString());
                    printout.flush();
                    printout.close();
                    //Log.i("STATUS", String.valueOf(connection.getResponseCode()));
                    //Log.i("MSG" , connection.getResponseMessage());

                    //int resp = connection.getResponseCode();
                    if (connection.getResponseCode() == 200){
                        queryResp =  true;
                    }else{
                        queryResp = false;
                    }
                    connection.disconnect();
                }catch (Exception e){
                    //Log.e("ERROR send data:", e.getMessage());
                    queryResp = false;
                    e.printStackTrace();
                }
            //}
       // });
       // thread.setName("sendData");
       // thread.start();
        return queryResp;
    }


    @NonNull
    private JSONObject getImageRatingJsonObject(String userID, String imageID, int rating) {
        JSONObject userImageRating = new JSONObject();
        try {
            userImageRating.put("userId", userID);
            userImageRating.put("imageId", imageID);
            userImageRating.put("rating", rating + "");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return userImageRating;
    }

    private JSONObject getUserJsonObject(String userID, String age, String gender) {
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("userId", userID);
            userJson.put("age", age);
            userJson.put("sex", gender);
            userJson.put("date", getCurrentTimestamp());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return userJson;
    }

    private JSONObject getActionsJsonObject(ActionObject actionObject){
        JSONObject actionJson = new JSONObject();
        try {
            actionJson.put("userId", actionObject.getUserId());
            actionJson.put("sessionId", actionObject.getSessionId());
            actionJson.put("timestamp", actionObject.getTimeStamp());
            actionJson.put("total_screens", actionObject.getTotalScreens());
            actionJson.put("screen_order", actionObject.getScreenOrder());
            actionJson.put("time_to_pass", actionObject.getTimeToPass());
            actionJson.put("success", actionObject.isSuccess());
            actionJson.put("selected_images", actionObject.getSelected());
            actionJson.put("shown_images", actionObject.getShown());
            actionJson.put("top_rated_images", actionObject.getTopRated());

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return actionJson;
    }

    public String getCurrentTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String stringTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(timestamp);
        Log.i("Timestamp",stringTime );
        return stringTime;
    }

    //still waiting
    public synchronized String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    public String generateUniqueSessionId(){
        return UUID.randomUUID().toString().substring(0,8);
    }



}
