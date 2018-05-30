package com.fourteenfourhundred.critique.UI.Fragments;

import com.fourteenfourhundred.critique.UI.Fragments.AllProfileFragment;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BestProfileFragment extends AllProfileFragment {


    public BestProfileFragment(){


    }


    public boolean shouldRender(JSONObject post) throws JSONException {
        JSONArray votes=post.getJSONArray("votes");
        for(int i=0; i< votes.length();i++){
            JSONObject vote = votes.getJSONObject(i);
            if(vote.getString("username").equals(Data.getUsername()) && vote.getInt("vote")==1){
                return true;
            }
        }
        return false;
    }



}
