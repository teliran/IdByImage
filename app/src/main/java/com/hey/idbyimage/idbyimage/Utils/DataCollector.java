package com.hey.idbyimage.idbyimage.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;


import java.io.DataOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;


/**
 * DataCollector - Single Object intended to collect the user's data,
 * Organize it and send it to the server.
 */
public class DataCollector {

    private static SharedPreferences actionsDataPref;
    private static  SharedPreferences userDataPref;
    //private static Context context;
    private static  final String SERVER_URL = "http://132.72.23.63:3001";
    private static DataCollector dataCollector = null;
    private int serverStatus;
    private boolean queryResp;
    private HashMap<String, Integer> allImagesRatings;
    private ArrayList<JSONObject> allActions;
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
        /*try {
            actionsDataPref = context.getSharedPreferences("actionsDataPrefs", Context.MODE_PRIVATE);
        }catch(Exception e){
            System.out.println("Unit Test");
        }*/

    }

    public static DataCollector getDataCollectorInstance(){
        if (dataCollector == null){
            dataCollector = new DataCollector();
        }
        return  dataCollector;
    }
    public void setSharedPref(SharedPreferences prf){
        this.actionsDataPref = prf;
    }

    public void setUserDataPref(SharedPreferences prf){
        this.userDataPref = prf;
    }
    /*public static void setContext(Context context) {
        DataCollector.context = context;
    }*/

    public boolean sendActionsDataToServer(ActionObject actions){
        JSONObject userActions = getActionsJsonObject(actions);
        if (!sendDataToServer(userActions, "/actions")){
            saveToActionsDataPref(userActions);
            return false;
        }
        return true;
    }

    public boolean sendActionsDataToServer(ActionObject actions){
        if (serverStatus != 200){
            return false;
        }
        JSONObject userActions = getActionsJsonObject(actions);
        if (!sendDataToServer(userActions, "/actions")){
            return false;
        }
        return true;
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
        JSONObject userJson = getUserJsonObject(userID, age, gender);
        if (serverStatus!=200){
            saveToUserDataPref(userJson);
            return false;
        }
        //JSONObject userJson = getUserJsonObject(userID, age, gender);
        if (!sendDataToServer(userJson, "/users")){
            saveToUserDataPref(userJson);
            return false;
        }
        return true;
    }

    public boolean sendStoredUserActions(){
        if (serverStatus!=200){
            return false;
        }
        if (allActions!=null){
            for (JSONObject o: allActions){
                if (!sendDataToServer(o, "/actions")){
                    saveToActionsDataPref(o);
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public boolean sendStoredUserData(){
        if (serverStatus!=200){
            return false;
        }
        String jsonString = getFromUserDataPref();
        if (jsonString == null){
            return false;
        }
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        if (!sendDataToServer(jsonObject,"/users")){
            saveToUserDataPref(jsonObject);
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
            actionJson.put("selected_images", actionObject.getSelected().toString());
            actionJson.put("shown_images", actionObject.getShown().toString());
            actionJson.put("top_rated_images", actionObject.getTopRated().toString());

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

    private void saveToActionsDataPref(JSONObject data){
        SharedPreferences.Editor editor = actionsDataPref.edit();
        editor.putString(data.toString(), getCurrentTimestamp());
        Log.i("saveToActionsDataPref: ", data.toString());
        editor.commit();
    }

    private void saveToUserDataPref(JSONObject data){
        SharedPreferences.Editor editor = userDataPref.edit();
        editor.putString("JSON_STRING", data.toString());
        Log.i("saveToUserDataPref: ", data.toString());
        editor.commit();
    }

    public boolean getFromActionsDataPref(){
        if (serverStatus!=200){
            return false;
        }
        HashMap<String, String> prefsActions = (HashMap<String, String>) actionsDataPref.getAll();
        if (prefsActions == null)
            return false;
        if (!prefsActions.isEmpty()){
            allActions = new ArrayList<>();
            for (String jsonString: prefsActions.keySet()){
                JSONObject json;
                try {
                    json = new JSONObject(jsonString);
                    allActions.add(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            SharedPreferences.Editor editor = actionsDataPref.edit();
            editor.clear();
            editor.commit();
            return true;
        }
        return false;
    }

    public String getFromUserDataPref(){
        if (serverStatus!=200){
            return null;
        }
        String jsonString = userDataPref.getString("JSON_STRING", null);
        SharedPreferences.Editor editor = userDataPref.edit();
        editor.clear();
        editor.commit();
        return jsonString;
    }



}
