package com.fourteenfourhundred.critique.API;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

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


    public void execute(final Util.Callback callback){
        responseListener = new Response.Listener<JSONObject>(){
            public void onResponse(JSONObject response) {
                callback.onResponse(response);
            }
        };

        errorListener = new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.getMessage());
            }
        };

        try {
            api.queue.add(new JsonObjectRequest(Request.Method.POST, Data.getURL()+getURL(), getParams(),responseListener,errorListener));
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
