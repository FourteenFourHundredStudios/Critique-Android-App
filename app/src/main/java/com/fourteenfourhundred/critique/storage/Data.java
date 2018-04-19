package com.fourteenfourhundred.critique.storage;

import android.util.Log;
import android.widget.ListView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.activities.HomeActivity;
import com.fourteenfourhundred.critique.activities.SelectMutualsActivity;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.util.SerializableJSONArray;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Marc on 3/6/18.
 */

public class Data {


    public static DataSerializer dataSerializer;
    //private static final long serialVersionUID = 1L;
    public static API backgroundApi;


    public static class DataSerializer implements Serializable{
        public String apiKey="apples!";
        //public String url="http://10.0.0.4:5000/";
        public String url="http://75.102.240.231:5000/";
        public String username=null;
        public int score=-1;
        public SerializableJSONArray mutuals = new SerializableJSONArray();
    }


    static {
        Data.dataSerializer = new Data.DataSerializer();
        if(Util.isEmulator()){
            dataSerializer.url="http://10.0.2.2:5000/";
        }
    }

    public static String getApiKey(){
        return dataSerializer.apiKey;
    }

    public static String getURL(){
        return dataSerializer.url;
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

    public static void setMutuals(JSONArray mutuals){
        dataSerializer.mutuals=new SerializableJSONArray(mutuals);
    }

    public static void setScore(int score){
        dataSerializer.score=score;
    }

    public static boolean isDebugMode(){
        return true;
    }

    public static void loadBackgroundData(final API api){
        new ApiRequest.GetMutualsRequest(api).execute(new Util.Callback(){
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("status").equals("ok")){
                        dataSerializer.mutuals=new SerializableJSONArray(response.get("mutuals").toString());
                        Storage.saveData(api.activity.getApplicationContext());
                    }else{
                        //idk what activity to show error in, so imma just close the app
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }




}
