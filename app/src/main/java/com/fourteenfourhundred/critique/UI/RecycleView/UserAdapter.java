package com.fourteenfourhundred.critique.UI.RecycleView;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fourteenfourhundred.critique.Framework.API.ApiRequest;
import com.fourteenfourhundred.critique.Framework.Models.User;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONObject;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private List<User> users;
    public boolean isSelection=false;
    public ChildItemClickListener clickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public View view;
        public TextView username;
        public TextView points;
        public ImageView profPic;

        public Button mutualButton;
        public Button pendingButton;
        public Button followButton;
        public ProgressBar buttonProgress;

        public CheckBox isSelected;


        public String usernameVal="";

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.username = view.findViewById(R.id.title);
            this.points = view.findViewById(R.id.caption);
            this.profPic = view.findViewById(R.id.picture);
            this.mutualButton=view.findViewById(R.id.mutualButton);
            this.pendingButton=view.findViewById(R.id.pendingButton);
            this.followButton=view.findViewById(R.id.followButton);
            this.buttonProgress=view.findViewById(R.id.buttonProgress);


            this.isSelected=view.findViewById(R.id.isSelected);



            mutualButton.setOnClickListener(this);
            pendingButton.setOnClickListener(this);
            followButton.setOnClickListener(this);



        }

        public void setUsername(String user){
            usernameVal=user;
        }

        @Override
        public void onClick(final View view) {



            switch (view.getId()){
                case R.id.followButton:
                    startButtonLoad((Button) view);

                    new ApiRequest.FollowRequest(Data.backgroundApi,usernameVal,true).execute(__ ->{

                        //Log.e("OK....","here");

                        pendingButton.setText("pending");
                        pendingButton.setVisibility(View.VISIBLE);
                        mutualButton.setVisibility(View.GONE);
                        followButton.setVisibility(View.GONE);
                        buttonProgress.setVisibility(View.GONE);

                    });

                    break;

                case R.id.mutualButton:
                    Util.showYesNoDialog(view.getContext(),"Are you sure you want to unfollow "+usernameVal+"?",__ -> {
                        startButtonLoad((Button) view);
                        new ApiRequest.FollowRequest(Data.backgroundApi,usernameVal,false).execute(___ -> {

                            pendingButton.setVisibility(View.GONE);
                            mutualButton.setVisibility(View.GONE);
                            followButton.setText("follow");
                            followButton.setVisibility(View.VISIBLE);
                            buttonProgress.setVisibility(View.GONE);
                        });
                    });


                    break;


                case R.id.pendingButton:
                    Util.showYesNoDialog(view.getContext(),"Are you sure you want to unfollow "+usernameVal+"?", unfollow ->{
                            if((Boolean) unfollow){

                                startButtonLoad((Button) view);

                                new ApiRequest.FollowRequest(Data.backgroundApi,usernameVal,false).execute(__ -> {
                                    pendingButton.setVisibility(View.GONE);
                                    mutualButton.setVisibility(View.GONE);
                                    followButton.setText("follow");
                                    followButton.setVisibility(View.VISIBLE);
                                    buttonProgress.setVisibility(View.GONE);

                                });

                        }
                    });


                    break;
            }
        }



        public void startButtonLoad(Button me){
            buttonProgress.getIndeterminateDrawable().setColorFilter(me.getCurrentTextColor(), PorterDuff.Mode.SRC_IN);
            me.setText("");
            buttonProgress.setVisibility(View.VISIBLE);
        }

    }



    public UserAdapter(List<User> users) {
        this.users = users;
    }

    public UserAdapter(List<User> users,boolean isSelection, ChildItemClickListener clickListener) {
        this.users = users;
        this.isSelection=isSelection;
        this.clickListener=clickListener;
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

            view.setUsername(user.getName());

            if(user.isMutual()){
                view.mutualButton.setText("mutual");
                view.mutualButton.setVisibility(View.VISIBLE);
                view.pendingButton.setVisibility(View.GONE);
                view.followButton.setVisibility(View.GONE);

            }else if(!Data.isFollowing(user.getName())) {
                view.mutualButton.setVisibility(View.GONE);
                view.pendingButton.setVisibility(View.GONE);
                view.followButton.setText("follow");
                view.followButton.setVisibility(View.VISIBLE);
            }else{
                view.mutualButton.setVisibility(View.GONE);
                view.pendingButton.setText("pending");
                view.pendingButton.setVisibility(View.VISIBLE);
                view.followButton.setVisibility(View.GONE);

            }

            new ApiRequest.GetPatchRequest(Data.backgroundApi,user.getName()).execute(img -> {
                    view.profPic.setImageBitmap((Bitmap) img);
            });


            if(isSelection){
                view.isSelected.setVisibility(View.VISIBLE);
                view.view.setOnClickListener((View v)->{
                    clickListener.onClick(v,user);
                    view.isSelected.toggle();
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public interface ChildItemClickListener {
        public void onClick(View v, User user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }



}