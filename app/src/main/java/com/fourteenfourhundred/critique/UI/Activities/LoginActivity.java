package com.fourteenfourhundred.critique.UI.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.storage.Storage;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    API loginAPI;


    public void login(final String username, String password){

        new ApiRequest.LoginRequest(loginAPI,username,password).execute(new Util.Callback(){
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.get("status").equals("error")) {
                        Util.showDialog(LoginActivity.this, response.getString("message"));
                    } else {
                        startApp(response,username);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    public void startApp(JSONObject response,String username){

        try{
            Data.setApiKey(response.getString("apiKey"));
            Data.setUsername(username);
            Data.setMutuals(response.getJSONArray("mutuals"));
            Data.setScore(response.getInt("score"));
            Storage.saveData(getApplicationContext());


            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);


         loginAPI = new API(this);



        final Button button = findViewById(R.id.loginButton);
        final EditText usernameBox = findViewById(R.id.usernameBox);
        final EditText passwordBox = findViewById(R.id.passwordBox);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                login(usernameBox.getText().toString(),passwordBox.getText().toString());


            }
        });


    }



}
