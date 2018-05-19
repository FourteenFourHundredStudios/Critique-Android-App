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



    }

    public void onRemoved(){
        view.destroy();

    }

    public void onPresented(){


        view= ((WebView) rootView.findViewById(R.id.webcontent));


        view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });

        /*
        ((View)view.getParent()).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });*/
        view.loadUrl(getContent());
    }


}
