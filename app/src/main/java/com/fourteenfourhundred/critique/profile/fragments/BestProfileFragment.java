package com.fourteenfourhundred.critique.profile.fragments;

import com.fourteenfourhundred.critique.profile.fragments.AllProfileFragment;
import com.fourteenfourhundred.critique.storage.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class BestProfileFragment extends AllProfileFragment {


    public BestProfileFragment(){


    }


    public boolean shouldRender(JSONObject post) throws JSONException {

        return post.getJSONObject("votes").getInt(Data.username)==1;
    }

}
