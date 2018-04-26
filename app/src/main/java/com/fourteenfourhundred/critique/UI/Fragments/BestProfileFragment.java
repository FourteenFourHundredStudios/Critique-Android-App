package com.fourteenfourhundred.critique.UI.Fragments;

import com.fourteenfourhundred.critique.UI.Fragments.AllProfileFragment;
import com.fourteenfourhundred.critique.storage.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class BestProfileFragment extends AllProfileFragment {


    public BestProfileFragment(){


    }


    public boolean shouldRender(JSONObject post) throws JSONException {

        return post.getJSONObject("votes").getInt(Data.getUsername())==1;
    }

}
