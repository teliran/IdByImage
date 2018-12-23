package com.hey.idbyimage.idbyimage.ControllerClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SetupControllerImpl implements SetupController{
    //Field of DB

    public SetupControllerImpl(){
        //init DB instance
    }

    @Override
    public boolean rateImage(String imgName, int rating) {
        //send to DB (HashImgName(imgName, rating)
        return false;
    }

    public String HashImgName(String imgName){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(imgName.getBytes(StandardCharsets.UTF_8));
        return hash.toString();
    }
}
