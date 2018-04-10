package com.fourteenfourhundred.critique.activities.HomeScreen.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.views.PostItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BestProfileFragment extends AllProfileFragment{


    public BestProfileFragment(){


    }


    public boolean shouldRender(JSONObject post) throws JSONException {

        return post.getJSONObject("votes").getInt(Data.username)==1;
    }

}
