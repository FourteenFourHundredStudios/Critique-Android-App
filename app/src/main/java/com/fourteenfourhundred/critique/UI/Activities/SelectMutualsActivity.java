package com.fourteenfourhundred.critique.UI.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.fourteenfourhundred.critique.Framework.API.API;
import com.fourteenfourhundred.critique.Framework.Models.User;
import com.fourteenfourhundred.critique.UI.RecycleView.RecycleViewManager;
import com.fourteenfourhundred.critique.UI.RecycleView.UserAdapter;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectMutualsActivity extends AppCompatActivity {

    ListView mutualsList;

    User mutuals[];

    final Activity me = this;

    public API api;

    RecycleViewManager view;

    ArrayList<String> selected = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mutuals);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Mutuals");

        api= new API(this);

        populateMutualsList();


    }


    public void populateMutualsList(){
        RecyclerView mutualsList=(RecyclerView)findViewById(R.id.mutualsList);


        List<User> mutuals=User.jsonToUserList(Data.getMutuals());
        UserAdapter userAdapter = new UserAdapter(mutuals,true,(View view,String username)->{
            if(selected.indexOf(username)==-1){
                selected.add(username);
            }else{
                selected.remove(username);
            }
        });
        view = new RecycleViewManager(mutualsList, this,userAdapter,mutuals);
    }




    @Override
    public void onBackPressed() {
        finish();
       // Intent returnIntent = new Intent();
        //returnIntent.putExtra("selected",to.toString());
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }


    //in the future it would make more sense to have this Activity return the selected users so it is reusable
    public void send(){
        JSONArray to = new JSONArray();


        //TODO

        for(String username:selected)to.put(username);



        Intent returnIntent = new Intent();
        //Util.showDialog(this,to.toString());
        returnIntent.putExtra("selected",to.toString());
        setResult(Activity.RESULT_OK,returnIntent);

        finish();
        /*
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        API.createPost(this,to,"text",title,content,new Response.Listener<JSONObject>(){
            public void onResponse(JSONObject response) {
                try {

                    String status = response.getString("status");
                    if(status.equals("error")){
                        String msg = response.getString("message");
                        Util.showDialog(me, msg);
                    }else{
                        setResult(2);
                        finish();
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });*/
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.sendButton:
                send();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mutuals_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }





}
