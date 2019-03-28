package com.hey.idbyimage.idbyimage.Utils;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * DataCollector - Single Object intended to collect the user's data,
 * Organize it and send it to the server.
 */
public class DataCollector {

    private static  final String SERVER_URL = "http://132.72.23.63:3001";
    //private static final String PORT = "3001";
    private static DataCollector dataCollector = new DataCollector();
    private boolean serverStatus;


    private DataCollector(){
        this.serverStatus = checkServerStatus();
    }

    public static DataCollector getDataCollectorInstance(){
        return  dataCollector;
    }

    private boolean checkServerStatus(){
        //Create a URL object holding our url
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
            return true;
        }catch(Exception e){
            String s = e.getMessage();
            return false;
        }
    }

    public boolean getServerStatus(){
        return this.serverStatus;
    }








}
