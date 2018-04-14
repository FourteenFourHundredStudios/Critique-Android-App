package com.fourteenfourhundred.critique.profile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.activities.HomeActivity;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.views.PostItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AllProfileFragment extends Fragment {

    View rootView;
    LinearLayout content;

    public JSONArray archive;
    SwipeRefreshLayout swipeRefreshLayout;
    public int page=0;
    boolean moreContent=true;
    boolean loadingContent=false;
    boolean firstLoad=true;
    ProgressBar more;
    API api;


    public AllProfileFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        rootView = inflater.inflate(R.layout.fragment_all_profile, container, false);
        content = rootView.findViewById(R.id.allFragmentContent);

        swipeRefreshLayout=rootView.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );



        final ScrollView scrollView = rootView.findViewById(R.id.allScrollview);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                // if diff is zero, then the bottom has been reached


                //Log.e("diff",diff+"");
                if (diff <650 && moreContent && !swipeRefreshLayout.isRefreshing() &&!loadingContent) {
                    //Util.showDialog(getActivity(),"YUP");
                    //Log.e("diff",diff+"");
                    loadingContent=true;
                    loadNewContent();
                }
            }
        });


        api=((HomeActivity) getActivity()).ProfileApi;

        more = new ProgressBar(getContext());
        content.addView(more);

        //api=new API(getActivity());



        refresh();

        return rootView;
    }



    public void refresh(){
        moreContent=true;
        new ApiRequest.GetArchiveRequest(api,0,page+1).execute(new Util.Callback(){
            @Override
            public void onResponse(final JSONObject response) {
                try {

                    archive = response.getJSONArray("archive");


                    content.removeAllViews();
                    render();


                    if(archive.length()==0){
                        content.removeAllViews();
                        TextView t = new TextView(getContext());
                        //t.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                        t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        t.setPadding(15,35,15,35);

                        t.setText("Theres nothing here :(");
                        content.addView(t);
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    if(more!=null)more.setVisibility(View.GONE);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        //Util.showDialog(getActivity(),"fiewjifwe");
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && firstLoad){
            //Util.showDialog(getActivity(),"FIRST SHOWING "+this.getClass().toString());
            //Log.e("WOO",this.getClass().toString());
        }
        if(isVisibleToUser){
            firstLoad=false;
        }

    }

    public void loadNewContent(){
        //Util.showDialog(getActivity(),"LOADING CONTENT");
        page++;

        more = new ProgressBar(getContext());
        content.addView(more);

        new ApiRequest.GetArchiveRequest(api,page,1).execute(new Util.Callback(){
            @Override
            public void onResponse(final JSONObject response) {
                try {

                    JSONArray newContent = response.getJSONArray("archive");
                    if(newContent.length()==0){
                        //Util.showDialog(getActivity(),"OUT");
                        moreContent=false;
                        if(more!=null)more.setVisibility(View.GONE);
                        TextView t = new TextView(getContext());
                        //t.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                       t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        t.setPadding(15,15,15,30);

                        t.setText("Theres nothing here :(");
                        content.addView(t);

                    }
                    more.setVisibility(View.GONE);
                    for(int i=0;i<newContent.length();i++){
                        PostItemView item = new PostItemView(getContext(), api, newContent.getJSONObject(i).toString());
                        content.addView(item.getSelf());
                    }




                    loadingContent=false;

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    public boolean shouldRender(JSONObject e) throws JSONException {
        return true;
    }



    public void render(){
        try {


                for(int i=0;i<archive.length();i++){

                    JSONObject post = new JSONObject(archive.getString(i));

                    if(shouldRender(post)) {
                        PostItemView item = new PostItemView(getContext(), api, post.toString());
                        content.addView(item.getSelf());
                    }

                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
