package com.fourteenfourhundred.critique.Framework.Models;

import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.fourteenfourhundred.critique.Framework.API.API;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONObject;

public class WebPost extends Post{

    WebView view;

    public WebPost(JSONObject postData) {
        super(postData);
    }

    public WebPost(String username, String title, String id, String type, JSONObject votes, String content) {
        super(username, title, id, type, votes, content);
    }

    public int getViewId(){
        return R.layout.post_view_link;
    }

    public void modifyValues(API api){
        view= ((WebView) rootView.findViewById(R.id.webcontent));


        view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });

    }

    public void onRemoved(){
        view.destroy();
    }


    public void onPresented(){



        view.loadUrl(getContent());
    }


}
