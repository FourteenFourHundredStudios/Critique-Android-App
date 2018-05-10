package com.fourteenfourhundred.critique.UI.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.Framework.API.API;
import com.fourteenfourhundred.critique.Framework.API.ApiRequest;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

/**
 * Created by Marc on 27/3/18.
 */

public class PostItemView extends PostView {



    public PostItemView(final Context context, API api,String post) {
        super(context,api,post);

    }






    public void init(){
        try {



            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.recycle_view_post_item, null, false);




            ((TextView) rootView.findViewById(R.id.postTitle)).setText(post.getString("title"));
            ((TextView) rootView.findViewById(R.id.postContent)).setText(post.getString("content"));



            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ApiRequest.GetPatchRequest(api,post.getString("username")).execute(new Util.Callback(){
                            public void onResponse(final Bitmap img) {
                                ((ImageView) rootView.findViewById(R.id.userPatch)).setImageBitmap(img);

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
