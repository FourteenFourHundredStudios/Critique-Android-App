package com.fourteenfourhundred.critique.UI.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.util.Util;

public class LinkPostView extends PostView {


    public LinkPostView(Context context, API api, String post) {
        super(context, api, post);
    }


    public void init(){
        try {


            //TODO FIX LATER


            /*
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.old_fragment_que_link, null, false);




            ((TextView) rootView.findViewById(R.id.postTitle)).setText(post.getString("title"));
            ((WebView) rootView.findViewById(R.id.linkView)).setWebViewClient(new WebViewClient());
            ((WebView) rootView.findViewById(R.id.linkView)).loadUrl(post.getString("content"));
            ((TextView) rootView.findViewById(R.id.postSender)).setText(post.getString("username"));

*/


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ApiRequest.GetPatchRequest(api,post.getString("username")).execute(new Util.Callback(){
                            public void onResponse(final Bitmap img) {



                                ((ImageView) rootView.findViewById(R.id.queProfilePic)).setImageBitmap(img);

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
