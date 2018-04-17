package com.fourteenfourhundred.critique.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.android.volley.Response;
import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectMutualsActivity extends AppCompatActivity {

    ListView mutualsList;

    User mutuals[];

    final Activity me = this;

    public API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mutuals);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Mutuals");

        api= new API(this);

        populateMutualsList();


    }


    public class User {

        JSONObject self;
        boolean selected = false;


        public User(String self){
            try {
                this.self=new JSONObject(self);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public boolean getSelected(){
            return selected;
        }

        void toggleSelected(){
            selected = !selected;
        }

        public String getUsername(){
            try {
                return self.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String isMutual(){
            try {
                return self.getString("isMutual");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }



    public void populateMutualsList(){


        new ApiRequest.GetMutualsRequest(api).execute(new Util.Callback(){
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("status").equals("ok")){
                        JSONArray mutualNames = new JSONArray(response.get("mutuals").toString());

                        mutuals= new User[mutualNames.length()];

                        for(int i=0;i<mutualNames.length();i++){
                            Log.e("f",mutualNames.get(i).toString());
                            mutuals[i]=new User(mutualNames.get(i).toString());
                        }

                        mutualsList = (ListView) findViewById(R.id.mutualsList);
                        CustomAdapter customAdapter = new CustomAdapter(me.getApplicationContext(), mutuals);
                        mutualsList.setAdapter(customAdapter);

                    }else{
                        Util.showDialog(me,response.get("message").toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

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
        for(User i : mutuals)if(i.getSelected())to.put(i.getUsername());

        //Util.showDialog(this, to.toString());

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


    class CustomAdapter extends BaseAdapter {
        Context context;
        User mutualsList[];
        LayoutInflater inflter;
        boolean[] checkBoxes;


        public CustomAdapter(Context applicationContext, User[] mutualsList) {
            this.context = context;
            this.mutualsList = mutualsList;
            inflter = (LayoutInflater.from(applicationContext));
            checkBoxes=new boolean[mutualsList.length];
        }

        @Override
        public int getCount() {
            return mutualsList.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.mutual_list_item, null);
            final CheckBox mutual = (CheckBox) view.findViewById(R.id.mutualName);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //mutual.setChecked(!mutual.isChecked());
                    mutual.toggle();
                    mutuals[i].toggleSelected();
                    //checkBoxes[i]=!checkBoxes[i];
                }

            });

            mutual.setText(mutualsList[i].getUsername());


            return view;
        }
    }


}
