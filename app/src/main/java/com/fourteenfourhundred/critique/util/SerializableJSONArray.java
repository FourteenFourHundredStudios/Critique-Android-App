package com.fourteenfourhundred.critique.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

public class SerializableJSONArray extends JSONArray implements Serializable{

    public SerializableJSONArray(String val) throws JSONException {
        super(val);
    }

    public SerializableJSONArray(){
        super();
    }

    public SerializableJSONArray(JSONArray ar){
        super();
        try {
            for(int i=0;i<ar.length();i++){
                put(ar.get(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void writeObject(java.io.ObjectOutputStream out)throws IOException {
        out.writeObject(super.toString());
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException {
        try {
            JSONArray val = new JSONArray((String) in.readObject());
            for(int i=0;i<val.length();i++){
                put(val.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
