package com.fourteenfourhundred.critique.Framework.API;

import android.app.Activity;
import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Callback;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.util.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc on 17/3/18.
 */

public class API {


    public Activity activity;
    public RequestQueue queue;

    public API(Activity activity){
        this.activity=activity;
        queue=Volley.newRequestQueue(activity);

    }






    public static void changePatch(final Activity me, final Bitmap img, final Callback.Response callback) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Data.getURL() + "setPatch",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            //callback.onResponse((Object)obj);
                            callback.onResponse(obj);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }


                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("apiKey", Data.getApiKey());
                return params;
            }


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











}
