package com.fourteenfourhundred.critique.UI.Views;

import android.util.Log;

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
