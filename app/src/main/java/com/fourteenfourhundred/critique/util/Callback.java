package com.fourteenfourhundred.critique.util;

import org.json.JSONObject;

public class Callback {


    public interface Response<T>{

        void onResponse(Object response);

    }

    public interface Completed extends Response{

        default void onFinished(){
            onResponse(null);
        }

    }

    public interface JSONResponse extends Response<JSONObject>{

        default void onDataResponse(JSONObject response){
           onResponse(response);
        }

    }



}