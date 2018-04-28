package com.fourteenfourhundred.critique.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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

        public void onResponse(String response){

        }

        public void onResponse(Object response){

        }

        public void onResponse(ArrayList<JSONObject> response){

        }


        public void onError(int code){

        }

        public void onError(String error){

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


    public static void showDialog(Context activity, String msg){
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

    public static void showInputDialog(Activity activity, String msg, final Callback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(msg);

        final EditText input = new EditText(activity);
        //input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onResponse(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }


}
