package com.fourteenfourhundred.critique.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Marc on 3/5/18.
 */

public class Util {




    public static JSONObject makeJson(Object[]... params){
        try {
            JSONObject f = new JSONObject();
            for (int i =0; i < params.length;i++) {
                f.put((String)params[i][0], params[i][1]);
            }
            return f;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static class Callback {

        public void onResponse(JSONObject response){

        }

        public void onFinished(){

        }

        public void onFinished(boolean state){

        }

        public void onResponse(Bitmap image){

        }



        public void onError(int code){

        }

    }

    public static void postRequest(Activity activity,String url,JSONObject data,Response.Listener rl,Response.ErrorListener re){
        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, data,rl,re);
        queue.add(getRequest);
    }



    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



    public static String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);


        return temp;
    }


    public static void showDialog(Activity activity,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
