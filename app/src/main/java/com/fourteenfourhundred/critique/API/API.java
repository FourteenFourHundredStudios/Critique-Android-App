package com.fourteenfourhundred.critique.API;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.util.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc on 17/3/18.
 */

public class API {

    /*
    CONVERT ALL OF THESE API CALLS TO USE CALLBACK CLASS
     */

    Activity activity;
    RequestQueue queue;

    public API(Activity activity){
        this.activity=activity;
        queue=Volley.newRequestQueue(activity);
    }

    public static void createPost(final Activity me,JSONArray recipients,String type,String title,String content, Response.Listener<JSONObject> callback){
        JSONObject params = Util.makeJson(
                new Object[]{"apiKey", Data.apiKey},
                new Object[]{"to",recipients},
                new Object[]{"type",type},
                new Object[]{"content",content},
                new Object[]{"title",title}
        );
        //Util.showDialog(me,loginInfo.toString());
        Util.postRequest(me,Data.url+"sendPost", params, callback,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Util.showDialog(me, "Connectivity error probably!");
                    }
                }
        );

    }


    public void getPatch(final Activity me, String username,final Util.Callback callback){


       // Log.e("IMAGE PATH",Data.url+"getPatch/"+Data.apiKey+"/"+username);

        ImageRequest request = new ImageRequest(Data.url+"getPatch/"+Data.apiKey+"/"+username,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {


                        callback.onResponse(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });
        queue.add(request);
    }





    public static void changePatch(final Activity me, final Bitmap img, final Util.Callback callback) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Data.url + "setPatch",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            callback.onResponse(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("apiKey", Data.apiKey);
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();

                 Bitmap i=img;

                 i = Bitmap.createScaledBitmap(img, 120, 120, false);

                params.put("pic", new DataPart(imagename + ".png", Util.getFileDataFromDrawable(i)));
                return params;
            }
        };


        Volley.newRequestQueue(me).add(volleyMultipartRequest);


    }


    public static void castVotes(final Activity me, JSONArray votes, final Util.Callback callback){

        try {




            JSONObject params = Util.makeJson(
                    new Object[]{"apiKey", Data.apiKey},
                    new Object[]{"votes", votes}
            );


            //Util.showDialog(me,loginInfo.toString());
            Util.postRequest(me, Data.url + "castVotes", params, new Response.Listener<JSONObject>(){

                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onResponse(response);
                        }
                    },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Util.showDialog(me, "Connectivity error probably!");
                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void getQue(final Activity me, final Util.Callback callback){

        try {

            /*
            JSONArray votes = new JSONArray();

            if (Data.lastPost.length() > 0) {
                for (int i = 0; i < Data.lastPost.length(); i++) {

                    votes.put(Util.makeJson(
                            new Object[]{"id", Data.lastPost.get(i).toString()},
                            new Object[]{"vote", Data.lastVote}
                    ));
                }
            }*/

            JSONObject params = Util.makeJson(
                    new Object[]{"apiKey", Data.apiKey}
            );
            //Util.showDialog(me,loginInfo.toString());
                    Util.postRequest(me, Data.url + "getPosts", params, new Response.Listener<JSONObject>(){

                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onResponse(response);
                        }
                    },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Util.showDialog(me, "Connectivity error probably!");
                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void doSearch(final Activity me, String search,final Util.Callback callback){

        try {

            Util.postRequest(me, Data.url + "search/"+search, new JSONObject(), new Response.Listener<JSONObject>(){

                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onResponse(response);
                        }
                    },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Util.showDialog(me, "Connectivity error probably!");
                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void getMutuals(final Activity me, Response.Listener<JSONObject> callback){
        JSONObject params = Util.makeJson(
                new Object[]{"apiKey",Data.apiKey}
        );
        //Util.showDialog(me,loginInfo.toString());
        Util.postRequest(me,Data.url+"getMutuals", params, callback,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Util.showDialog(me, "Connectivity error probably!");
                    }
                }
        );

    }


}
