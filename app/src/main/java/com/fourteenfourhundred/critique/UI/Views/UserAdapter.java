package com.fourteenfourhundred.critique.UI.Views;

import android.graphics.Bitmap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> users;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView username;
        public TextView points;
        public ImageView profPic;

        public Button mutualButton;
        public Button notMutualButton;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.username = view.findViewById(R.id.username);
            this.points = view.findViewById(R.id.points);
            this.profPic = view.findViewById(R.id.profPic);
            this.mutualButton=view.findViewById(R.id.mutualButton);
            this.notMutualButton=view.findViewById(R.id.notMutualButton);



        }
    }


    public UserAdapter(List<User> users) {
        this.users = users;
    }


    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_user_item, parent, false);
        ViewHolder vh = new ViewHolder(v);



        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder view, int position) {

        try {
            User user=users.get(position);

            view.username.setText(user.getName());
            view.points.setText(user.getScore()+" points");

            if(user.isMutual()){
                view.mutualButton.setVisibility(View.VISIBLE);
                view.notMutualButton.setVisibility(View.GONE);
            }else{
                view.mutualButton.setVisibility(View.GONE);
                view.notMutualButton.setVisibility(View.VISIBLE);

            }

            new ApiRequest.GetPatchRequest(Data.backgroundApi,user.getName()).execute(new Util.Callback(){
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
        return users.size();
    }

}