package com.fourteenfourhundred.critique.API;

import android.graphics.Bitmap;
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

public class ApiRequest{



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

        @Override
        public String getURL() {
            return "sendPost";
        }

        @Override
        public JSONObject getParams() throws JSONException {
            JSONObject params=new JSONObject();
            params.put("to",to);
            params.put("type",type);
            params.put("content",content);
            params.put("title",title);
            params.put("apiKey", Data.apiKey);
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
            params.put("apiKey", Data.apiKey);
            return params;
        }

    }


    public static class GetArchiveRequest extends GenericRequest{

        public int page;

        public GetArchiveRequest(API api,int page) {
            super(api);

            this.page=page;
        }

        @Override
        public String getURL() {
            return "getArchive";
        }

        @Override
        public JSONObject getParams() throws JSONException {
            JSONObject params=new JSONObject();
            params.put("page",page);
            params.put("apiKey", Data.apiKey);
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

        public FollowRequest(API api,String username) {
            super(api);
            this.username=username;
        }

        @Override
        public JSONObject getParams() throws JSONException {
            JSONObject params=new JSONObject();
            params.put("user",username);
            params.put("apiKey", Data.apiKey);
            return params;
        }

        @Override
        public String getURL() {
            return "follow";
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
            return "getPatch/"+Data.apiKey+"/"+username;
        }

        public void execute(final Util.Callback callback){
            responseListener = new Response.Listener<Bitmap>(){
                public void onResponse(Bitmap response) {
                    callback.onResponse(response);
                }
            };

            errorListener = new Response.ErrorListener(){
                public void onErrorResponse(VolleyError error) {
                    callback.onError(error.networkResponse.toString());
                }
            };

            try {
                api.queue.add(new ImageRequest( Data.url+getURL(),responseListener, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,errorListener));
            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }



}
