package com.fourteenfourhundred.critique.Framework.Models;

import com.fourteenfourhundred.critique.storage.Data;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class User {


    private String name;
    private int score;


    public User(String name,int score){
        this.name=name;
        this.score=score;


    }




    public boolean isMutual(){
        boolean isMutual=false;
        try {

            JSONArray mutuals = Data.getMutuals();
            for (int i = 0; i < mutuals.length(); i++) {
                if (mutuals.getJSONObject(i).getString("username").equals(name)) {
                    isMutual = mutuals.getJSONObject(i).getBoolean("isMutual");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return isMutual;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }


    public static List<User> jsonToUserList(JSONArray users){
       // Log.e("okay",users.toString());
        List<User> list = new ArrayList<User>();
        try {

            for (int i = 0; i < users.length(); i++) {
                list.add(new User(users.getJSONObject(i).getString("username"), users.getJSONObject(i).getInt("score")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }







}
