package com.fourteenfourhundred.critique.UI.Views;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.Framework.API.ApiRequest;
import com.fourteenfourhundred.critique.Framework.Models.Post;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private List<Post> posts;




    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        public View view;
        public TextView username;
        public TextView votes;
        public TextView title;
        public ImageView profPic;





        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.username = view.findViewById(R.id.username);
            this.title = view.findViewById(R.id.title);
            this.votes = view.findViewById(R.id.votes);
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

           // Log.e("here","yeah");

            view.title.setText(post.getTitle());
            view.username.setText("posted by "+post.getUsername());
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