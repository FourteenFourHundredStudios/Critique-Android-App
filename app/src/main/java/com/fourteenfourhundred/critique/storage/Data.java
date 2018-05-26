package com.fourteenfourhundred.critique.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fourteenfourhundred.critique.Framework.API.API;
import com.fourteenfourhundred.critique.Framework.API.ApiRequest;
import com.fourteenfourhundred.critique.util.SerializableJSONArray;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Marc on 3/6/18.
 */

public class Data {


    public static DataSerializer dataSerializer;
    //private static final long serialVersionUID = 1L;
    public static API backgroundApi;

    //public static String url="http://10.0.0.4:5000/";
    public static String url="http://75.102.230.66:5000/";
    //public static String url="http://75.102.218.43:5000/";


    public static boolean debug=true;

    public static class DataSerializer implements Serializable{
        public String apiKey="apples!";
        public String username=null;
        public int score=-1;
        public SerializableJSONArray mutuals = new SerializableJSONArray();
    }


    static {
        Data.dataSerializer = new Data.DataSerializer();
        if(Util.isEmulator()){
            Log.e("Messsage","This is emulator");
            url="http://10.0.2.2:5000/";
        }
    }

    public static String getApiKey(){
        return dataSerializer.apiKey;
    }

    public static String getURL(){
        return url;
    }

    public static String getUsername(){
        return dataSerializer.username;
    }

    public static int getScore(){
        return dataSerializer.score;
    }

    public static JSONArray getMutuals(){
        return dataSerializer.mutuals;
    }

    public static void setApiKey(String apiKey){
        dataSerializer.apiKey=apiKey;
    }

    public static void setUsername(String username){
        dataSerializer.username=username;
    }

    public static boolean isFollowing(String username){
        try {
            for (int i = 0; i < dataSerializer.mutuals.length(); i++) {
                if (dataSerializer.mutuals.getJSONObject(i).getString("username").equals(username)) return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    public static void setMutuals(JSONArray mutuals){
        dataSerializer.mutuals=new SerializableJSONArray(mutuals);
    }

    public static void setScore(int score){
        dataSerializer.score=score;
    }

    public static boolean isDebugMode(){
        return true;
    }

    public static void nuke(final Activity me){
        if(debug) {
            Log.e("nuke", "nuking...");
            new ApiRequest.ResetRequest(new API(me)).execute(__ ->{
                Log.e("nuke", "server reset...");
                File file = me.getApplication().getFileStreamPath("userdata");
                if (file.exists()) file.delete();
                SharedPreferences sharedPref = me.getPreferences(Context.MODE_PRIVATE);
                sharedPref.edit().clear().commit();
                Log.e("nuke", "files deleted...");

            });
        }
    }

    public static void loadBackgroundData(final API api){
        new ApiRequest.GetMutualsRequest(api).execute( response -> {
            try {
                dataSerializer.mutuals = new SerializableJSONArray((String) response);
                Storage.saveData(api.activity.getApplicationContext());
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }



}
