package com.fourteenfourhundred.critique.API;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ApiRequest{


    public static void jsonRequestchain(Util.Callback cb,GenericRequest... requests){
        jsonRequestchainRecursive(requests,new ArrayList<JSONObject>(),cb);
    }


    public static void jsonRequestchainRecursive(GenericRequest[] requests, final ArrayList<JSONObject> values, final Util.Callback cb){

        final ArrayList<GenericRequest> requestsList = new ArrayList<GenericRequest>(Arrays.asList(requests));

        GenericRequest r = requestsList.remove(0);

        if(requestsList.size()>0) {
            Log.e("here","not size");
            r.execute(new Util.Callback() {
                public void onResponse(final JSONObject obj) {

                    GenericRequest[] requestArray = new GenericRequest[requestsList.size()];

                    values.add(obj);

                    GenericRequest[] p = (GenericRequest[]) requestsList.toArray();

                    ApiRequest.jsonRequestchainRecursive(p,values,cb);
                }
            });
        }else{
            Log.e("here","size");
            r.execute(new Util.Callback() {
                public void onResponse(JSONObject obj) {

                   cb.onResponse(values);
                }
            });
        }


    }


    public static class SendPostRequest  extends GenericRequest{

        public JSONArray to;
        public String type;
        public String content;
        public String title;


        public SendPostRequest(API api,JSONArray to,String type,String title,String content ) {
            super(api);

            this.to=to;
            this.type=type;
            this.content=content;
            this.title=title;
        }





        public String getURL() {
            return "sendPost";
        }





        public JSONObject getParams() throws JSONException {
            JSONObject params=new JSONObject();
            params.put("to",to);
            params.put("type",type);
            params.put("content",content);
            params.put("title",title);
            params.put("apiKey", Data.getApiKey());
            return params;
        }


    }



    public static class CastVotesRequest extends GenericRequest{

        public JSONArray votes;

        public CastVotesRequest(API api,JSONArray votes) {
            super(api);

            this.votes=votes;
        }

        @Override
        public String getURL() {
            return "castVotes";
        }

        @Override
        public JSONObject getParams() throws JSONException {
            JSONObject params=new JSONObject();
            params.put("votes",votes);
            params.put("apiKey", Data.getApiKey());
            return params;
        }

    }




    public static class GetArchiveRequest extends GenericRequest{

        public int page;
        public int count;

        public GetArchiveRequest(API api,int page,int count) {
            super(api);

            this.page=page;
            this.count=count;
        }

        @Override
        public String getURL() {
            return "getArchive";
        }

        @Override
        public JSONObject getParams() throws JSONException {
            JSONObject params=new JSONObject();
            params.put("page",page);
            params.put("count",count);
            params.put("apiKey", Data.getApiKey());
            return params;
        }

    }


    public static class GetQueRequest extends GenericRequest{

        public GetQueRequest(API api) {
            super(api);
        }

        @Override
        public String getURL() {
            return "getPosts";
        }

    }


    public static class ResetRequest extends GenericRequest{

        public ResetRequest(API api) {
            super(api);
        }

        @Override
        public String getURL() {
            return "reset";
        }

    }


    public static class GetMutualsRequest extends GenericRequest{

        public GetMutualsRequest(API api) {
            super(api);
        }

        @Override
        public String getURL() {
            return "getMutuals";
        }

    }

    public static class DoSearchRequest extends GenericRequest{

        public String search;

        public DoSearchRequest(API api,String search) {
            super(api);
            this.search=search;
        }

        @Override
        public String getURL() {
            return "search/"+search;
        }

    }

    public static class FollowRequest extends GenericRequest{

        public String username;
        public boolean following;

        public FollowRequest(API api,String username,boolean following) {
            super(api);
            this.username=username;
            this.following=following;
        }

        @Override
        public JSONObject getParams() throws JSONException {
            JSONObject params=new JSONObject();
            params.put("user",username);
            params.put("apiKey", Data.getApiKey());
            params.put("following", following);
            return params;
        }

        @Override
        public String getURL() {
            return "follow";
        }

    }

    public static class LoginRequest extends GenericRequest{

        public String username;
        public String password;

        public LoginRequest(API api,String username,String password) {
            super(api);
            this.username=username;
            this.password=password;
        }

        @Override
        public JSONObject getParams() throws JSONException {
            JSONObject params=new JSONObject();
            params.put("username",username);
            params.put("password",password);
            return params;
        }

        @Override
        public String getURL() {
            return "login";
        }

    }



    public static class GetPatchRequest extends GenericRequest{

        public String username;

        public GetPatchRequest(API api,String username) {
            super(api);
            this.username=username;
        }

        @Override
        public String getURL() {
            return "getPatch/"+Data.getApiKey()+"/"+username;
        }

        public void execute(final Util.Callback callback){
            responseListener = new Response.Listener<Bitmap>(){
                public void onResponse(Bitmap response) {
                    callback.onResponse(response);
                }
            };

            errorListener = new Response.ErrorListener(){
                public void onErrorResponse(VolleyError error) {
                    callback.onError(error.getMessage());

                    if(Data.isDebugMode())Util.showDialog(api.activity,error.getMessage());
                }
            };

            try {
                api.queue.add(new ImageRequest( Data.getURL()+getURL(),responseListener, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,errorListener));
            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }





}
