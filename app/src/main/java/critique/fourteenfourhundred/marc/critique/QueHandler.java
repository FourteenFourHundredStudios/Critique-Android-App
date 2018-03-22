package critique.fourteenfourhundred.marc.critique;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Marc on 22/3/18.
 */

public class QueHandler {

    public static int remainingInQue=0;
    public static ArrayList<JSONObject> que = new ArrayList<JSONObject>();


    public static void getNextInQue(final Activity me, final Callback callback){
        Log.i("COUNT",remainingInQue+"");
        if(remainingInQue==0){
            que.clear();
            API.getQue(me,new Callback(){
                public void onResponse(JSONObject object){
                    try{
                        if(object.getString("status").equals("ok")){
                            JSONArray posts=new JSONArray(object.get("message").toString());
                            if(posts.length()>0){
                                for(int i=0;i<posts.length();i++){
                                    que.add(new JSONObject(posts.get(i).toString()));
                                }
                                remainingInQue=que.size();
                                callback.onResponse(que.get((que.size())-remainingInQue));
                                remainingInQue--;
                            }else{
                                //no posts
                                callback.onError(1);
                            }
                        }else{
                            //error
                            Util.showDialog(me,object.getString("message"));
                            callback.onError(2);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }else{


            callback.onResponse(que.get((que.size())-remainingInQue));
            remainingInQue--;
        }
    }

}
