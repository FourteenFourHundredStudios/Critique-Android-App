package com.fourteenfourhundred.critique.Framework.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.Framework.API.API;
import com.fourteenfourhundred.critique.Framework.API.ApiRequest;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONObject;

public class Post {

    protected View rootView;
    private JSONObject postData;
    private String username;
    private String title;
    private String content;
    private String type;
    private String id;
    private JSONObject votes;
    private int voteTotal;


    public Post(JSONObject postData){
        try {
            this.postData=postData;
            this.username = postData.getString("username");
            this.title = postData.getString("title");
            this.id = postData.getString("_id");
            this.type = postData.getString("type");

            this.content = postData.getString("content");

            Object voteData = postData.get("votes");

            if(voteData instanceof Integer){
                voteTotal=(Integer) voteData;
                votes=new JSONObject();
            }else{
                votes=(JSONObject)voteData ;
                voteTotal = votes.length();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Post(String username,String title, String id, String type,JSONObject votes,String content){
        try {
            this.postData=new JSONObject();
            this.username = username;
            this.title = title;
            this.id = id;
            this.type = type;
            this.votes = votes;
            this.content = content;
            this.voteTotal = votes.length();
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

    public int getVoteTotal(){
        return voteTotal;
    }

    public String getType(){
        return type;
    }

    public String getAttribute(String attribute){
        try{
            return postData.get(attribute).toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public View getView(){
        return rootView;
    }

    public JSONObject getPostData() {
        return postData;
    }

    public void inflateView(API api, Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.post_view, null, false);

        ((TextView) rootView.findViewById(R.id.title)).setText(title);
        ((TextView) rootView.findViewById(R.id.content)).setText(content);
        ((TextView) rootView.findViewById(R.id.caption)).setText("posted by "+username);
        ((TextView) rootView.findViewById(R.id.vote_count)).setText(voteTotal + " votes");
        AsyncTask.execute(()->{
            try {
                new ApiRequest.GetPatchRequest(api,getUsername()).execute(new Util.Callback(){
                    public void onResponse(final Bitmap img) {
                        ((ImageView) rootView.findViewById(R.id.picture)).setImageBitmap(img);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



}
