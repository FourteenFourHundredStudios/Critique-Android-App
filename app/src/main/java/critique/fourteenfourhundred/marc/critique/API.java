package critique.fourteenfourhundred.marc.critique;

import android.app.Activity;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Marc on 17/3/18.
 */

public class API {



    public static void createPost(final Activity me,JSONArray recipients,String type,String title,String content, Response.Listener<JSONObject> callback){
        JSONObject loginInfo = Util.makeJson(
                new Object[]{"apiKey",Data.apiKey},
                new Object[]{"to",recipients},
                new Object[]{"type",type},
                new Object[]{"content",content},
                new Object[]{"title",title}
        );
        //Util.showDialog(me,loginInfo.toString());
        Util.postRequest(me,Data.url+"sendPost", loginInfo, callback,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Util.showDialog(me, "error");
                    }
                }
        );

    }


}
