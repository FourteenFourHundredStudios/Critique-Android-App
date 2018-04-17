package com.fourteenfourhundred.critique.storage;

import com.fourteenfourhundred.critique.util.Util;

/**
 * Created by Marc on 3/6/18.
 */

public class Data {



    public static String apiKey="1";
    //public static String url="http://75.102.252.211:5000/";
    //public static String url="http://10.0.2.2:5000/";
    //public static String url="http://75.102.240.56:5000/";
    public static String url="http://10.0.0.4:5000/";
    public static String username=null;

    static {
        if(Util.isEmulator()){
            url="http://10.0.2.2:5000/";
        }
    }

}
