package com.fourteenfourhundred.critique.UI.Views;

import org.json.JSONObject;

public class Post {

    String username;
    String title;
    String id;
    JSONObject postData;
    int votes;


    public Post(JSONObject postData){
        try {
            this.username = postData.getString("username");
            this.title = postData.getString("title");
            this.id = postData.getString("_id");
            this.votes = 0;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getUsername(){
        return username;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public int getVotes(){
        return votes;
    }

}
