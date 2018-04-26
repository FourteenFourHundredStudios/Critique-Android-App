package com.fourteenfourhundred.critique.storage;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Storage {


    public static void saveData(Context context){
        try {
            FileOutputStream fos = context.openFileOutput("userdata", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(Data.dataSerializer);
            os.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("message",e.getMessage());
        }

    }

    public static Data.DataSerializer getData(Context context) throws Exception {
            FileInputStream fis = context.openFileInput("userdata");
            ObjectInputStream is = new ObjectInputStream(fis);
            Data.DataSerializer data = (Data.DataSerializer) is.readObject();
            is.close();
            fis.close();
            return data;
    }

    public static boolean isUserData(Context context){
        File file = context.getFileStreamPath("userdata");
       // file.delete();
        return file.exists();
    }

}
