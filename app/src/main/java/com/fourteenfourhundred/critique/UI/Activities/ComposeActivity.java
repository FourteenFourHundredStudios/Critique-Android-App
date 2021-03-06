package com.fourteenfourhundred.critique.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.fourteenfourhundred.critique.Framework.API.API;
import com.fourteenfourhundred.critique.Framework.API.ApiRequest;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

public class ComposeActivity extends AppCompatActivity {

    String title;
    String content;
    //rename to activity
    public API api;
    public String type="text";
    public String linkURL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        api=new API(this);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE);

        getSupportActionBar().setCustomView(R.layout.compose_menu);

        getSupportActionBar().setDisplayShowTitleEnabled(false);



        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText e = findViewById(R.id.postTitle);
        e.requestFocus();
    }

    protected void onResume(){
        super.onResume();


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK){

            try {


                title= ((EditText)findViewById(R.id.postTitle)).getText().toString();

                if(type.equals("text")){
                    content= ((EditText)findViewById(R.id.postContent)).getText().toString();
                }else if(type.equals("link")){
                    content=linkURL;
                }

                new ApiRequest.SendPostRequest(api,new JSONArray(data.getStringExtra("selected")), type, title, content).execute( __ ->{

                    finish();

                });
                finish();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {


        finish();

        return false;
    }

    public void selectMutuals(){
        Intent intent = new Intent(getApplicationContext(), SelectMutualsActivity.class);

        startActivityForResult(intent,0);
    }

    public void selectLink(){
        Util.showInputDialog(this,"Website URL", s -> {
            type="link";
            linkURL=(String) s;
            selectMutuals();
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sendButton:
                selectMutuals();
                break;
            case R.id.sendLinkButton:
                selectLink();

                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.vote_menu,menu);

       // android.app.ActionBar actionBar = getActionBar();
        //actionBar.setCustomView(R.layout.compose_menu);



        return super.onCreateOptionsMenu(menu);
    }

}
