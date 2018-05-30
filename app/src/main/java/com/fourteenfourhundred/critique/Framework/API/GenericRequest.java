package com.fourteenfourhundred.critique.Framework.API;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Callback;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GenericRequest {

    public Response.Listener responseListener;
    public Response.ErrorListener errorListener;
    public API api;

    public GenericRequest(API api){
        this.api=api;
    }

    public JSONObject getParams() throws JSONException {
        JSONObject params=new JSONObject();
        params.put("apiKey", Data.getApiKey());
        return params;
    }

    public String getURL(){
        return null;
    }


    public void execute (final Callback.Response callback){

        responseListener = (Response.Listener<JSONObject>) response -> {
            try {
                if (response.getString("status").equals("error")){
                    Util.showDialog(api.activity,"Error from server: " + response.getString("response"));

                    //prob logout or something
                }else{

                    if(response.has("response")) {
                        Object data = new JSONTokener(response.get("response").toString()).nextValue();
                        if (data instanceof JSONObject) {
                            callback.onResponse(new JSONObject(data.toString()));
                        } else if (data instanceof JSONArray) {
                            callback.onResponse(new JSONArray(data.toString()));
                        }
                    }else if (response.getString("status").equals("ok")){
                        callback.onResponse(null);
                    }
                }
            }catch (Exception e){
                Log.e("FAILED AT ",getURL());
                e.printStackTrace();
            }

        };

        errorListener = new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };

        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Data.getURL()+getURL(), getParams(),responseListener,errorListener);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );
            api.queue.add(request);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



}
