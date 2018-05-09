package com.fourteenfourhundred.critique.UI.Views;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.UI.Activities.HomeActivity;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private List<Post> posts;




    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        public View view;
        public TextView username;
        public TextView points;
        public TextView content;
        public ImageView profPic;





        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.username = view.findViewById(R.id.username);
            this.content = view.findViewById(R.id.title);
            this.profPic = view.findViewById(R.id.userPatch);

        }

        @Override
        public void onClick(final View view) {


        }


    }



    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }


    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_post_item, parent, false);
        ViewHolder vh = new ViewHolder(v);



        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder view, int position) {

        try {
            Post post=posts.get(position);


            new ApiRequest.GetPatchRequest(Data.backgroundApi,post.getUsername()).execute(new Util.Callback(){
                public void onResponse(final Bitmap img) {
                    view.profPic.setImageBitmap(img);
                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return posts.size();
    }



}