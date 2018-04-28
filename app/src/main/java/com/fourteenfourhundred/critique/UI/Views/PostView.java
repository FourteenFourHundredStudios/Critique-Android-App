package com.fourteenfourhundred.critique.UI.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONObject;

/**
 * Created by Marc on 27/3/18.
 */

public class PostView extends View{

    public JSONObject post;
    public View rootView;
    Context context;
    public Bitmap patch;
    public API api;


    public PostView(final Context context, API api,String post) {
        super(context);

        try {
            this.post = new JSONObject(post);
            this.api=api;
            //rootView = View.inflate(context, R.layout.old_fragment_que, null);
            this.context=context;


           // ((Activity)getContext()).runOnUiThread(new Runnable() {
             //   public void run() {
                    init();
              //  }
            //});




        }catch (Exception e){
            e.printStackTrace();
        }
        //rootView = inflate(context, R.layout.old_fragment_que, this);
    }




//    public void g


    public JSONObject getPost(){
        return post;
    }


    public String getPostAttribute(String attr){

        try {
            return post.getString(attr);

        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public View getSelf(){
        return rootView;
    }

    public void setPost(String post){
        try {
           // Log.e("f","DID ITT");

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void init(){
        try {



            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.post_view, null, false);




           // ((TextView) rootView.findViewById(R.id.postTitle)).setText(post.getString("title"));
            ((TextView) rootView.findViewById(R.id.content)).setText(post.getString("content"));
            ((TextView) rootView.findViewById(R.id.username)).setText(post.getString("username"));
            ((TextView) rootView.findViewById(R.id.vote_count)).setText(post.getString("votes") + " votes");



            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ApiRequest.GetPatchRequest(api,post.getString("username")).execute(new Util.Callback(){
                        public void onResponse(final Bitmap img) {



                                ((ImageView) rootView.findViewById(R.id.queProfilePic)).setImageBitmap(img);
                                //((ImageView) rootView.findViewById(R.id.queProfilePic)).invalidate();


                                //Log.e("GETTING PIC","GETTING PIC POST"+post.getString("title"));


                            //((ImageView) rootView.findViewById(R.id.queProfilePic)).invalidate();

                        }
                    });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
