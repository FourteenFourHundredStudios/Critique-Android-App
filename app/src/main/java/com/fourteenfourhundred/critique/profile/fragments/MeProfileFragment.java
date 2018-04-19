package com.fourteenfourhundred.critique.profile.fragments;

import com.fourteenfourhundred.critique.profile.fragments.AllProfileFragment;
import com.fourteenfourhundred.critique.storage.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class MeProfileFragment extends AllProfileFragment {


    public MeProfileFragment(){


    }

    public boolean shouldRender(JSONObject post) throws JSONException {

        return post.getString("username").equals(Data.getUsername());
    }

}
