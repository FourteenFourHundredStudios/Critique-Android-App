package com.fourteenfourhundred.critique.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.storage.Storage;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class LaunchActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_launch);
        initApp();
    }

    public void initApp(){




        Class launch=HomeActivity.class;
        if(Storage.isUserData(getApplicationContext())){
            try {
                Data.dataSerializer = Storage.getData(getApplicationContext());

            }catch (Exception e){
                Log.e("error","Could not read serialization!");
                launch=LoginActivity.class;
            }
        }else{
            launch=LoginActivity.class;
        }

        Intent intent = new Intent(getApplicationContext(), launch);
        startActivity(intent);
        finish();
    }

}
