package com.hey.idbyimage.idbyimage.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is a Data class.
 * Its purpose is to store the collected data before being sent to the DataCollector.
 * In the DataCollector all the data will be converted to a JSONObject and will be sent
 * to the server.
 */
public class ActionObject {
    private String userId;
    private String sessionId;
    private String timeStamp;
    private String totalScreens;
    private String screenOrder;
    private String timeToPass;
    private boolean success;
    private List<String> selected;
    private List<String> shown;
    private List<String> topRated; //correct answer


    public ActionObject() {
        selected = new ArrayList<>();
        shown = new ArrayList<>();
        topRated = new ArrayList<>();
    }

    public String getTimeToPass() {
        return timeToPass;
    }

    public void setTimeToPass(String timeToPass) {
        this.timeToPass = timeToPass;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getTotalScreens() {
        return totalScreens;
    }

    public String getScreenOrder() {
        return screenOrder;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getSelected() {
        return selected;
    }

    public List<String> getShown() {
        return shown;
    }

    public List<String> getTopRated() {
        return topRated;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTotalScreens(String totalScreens) {
        this.totalScreens = totalScreens;
    }

    public void setScreenOrder(String screenOrder) {
        this.screenOrder = screenOrder;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setSelected(List<String> selected) {
        this.selected = selected;
    }

    public void setShown(List<String> shown) {
        this.shown = shown;
    }

    public void setTopRated(List<Map.Entry<String, Integer>> topRated) {
        if (!topRated.isEmpty()) {
            for (Map.Entry<String, Integer> entry : topRated) {
                this.topRated.add(entry.getKey());
            }
        }
    }
}
