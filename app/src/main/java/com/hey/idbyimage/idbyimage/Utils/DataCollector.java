package com.hey.idbyimage.idbyimage.Utils;

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

/**
 * DataCollector - Single Object intended to collect the user's data,
 * Organize it and send it to the server.
 */
public class DataCollector {

    private static  final String SERVER_URL = "http://132.72.23.63:3001";
    private static DataCollector dataCollector = new DataCollector();
    private boolean serverStatus;
    private HashMap<String, Integer> allImagesRatings;



    private DataCollector(){
        this.serverStatus = checkServerStatus();
        this.allImagesRatings = new HashMap<>();
    }

    public static DataCollector getDataCollectorInstance(){
        return  dataCollector;
    }

    private boolean checkServerStatus(){
        //Create a URL object holding our url : SERVER_URL+"/healthcheck"
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
            if(connection.getResponseCode() == 200) {
                return true;
            }
            return false;
        }catch(Exception e){
            //String s = e.getMessage();
            return false;
        }
    }

    public boolean getServerStatus(){
        return this.serverStatus;
    }

    public void setImageRatingsData(HashMap<String, Integer> map){
        this.allImagesRatings = map;
    }

    public boolean sendAllUserRatingsToServer(String userID){
        if (!serverStatus){
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
        if (!serverStatus){
            return false;
        }
        JSONObject userJson = getUserJsonObject(userID, age, gender);
        if (!sendDataToServer(userJson, "/users")){
            return false;
        }
        return true;
    }

    public boolean sendDataToServer(JSONObject jsonObj, String urlPrefix){
        if (!serverStatus){
            return false;
        }
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
            Log.i("JSON", jsonObj.toString());
            DataOutputStream printout = new DataOutputStream((connection.getOutputStream()));
            printout.writeBytes(jsonObj.toString());
            printout.flush();
            printout.close();
            Log.i("STATUS", String.valueOf(connection.getResponseCode()));
            Log.i("MSG" , connection.getResponseMessage());

            connection.disconnect();
            int resp = connection.getResponseCode();
            if (connection.getResponseCode() == 200){
                return true;
            }else{
                return false;
            }

        }catch (Exception e){
            Log.e("ERROR send data:", e.getMessage());
            return false;
        }
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

    private String getCurrentTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String stringTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(timestamp);
        Log.i("Timestamp",stringTime );
        return stringTime;
    }


}
