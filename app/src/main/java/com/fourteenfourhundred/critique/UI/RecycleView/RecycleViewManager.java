package com.fourteenfourhundred.critique.UI.RecycleView;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fourteenfourhundred.critique.Framework.Models.Post;
import com.fourteenfourhundred.critique.Framework.Models.User;

import java.util.ArrayList;
import java.util.List;


public class RecycleViewManager {


    RecyclerView view;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List data;

    public RecycleViewManager(RecyclerView view, Context context, RecyclerView.Adapter adapter,List data){
        this.view = view ;


       // view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        view.setLayoutManager(layoutManager);
        this.adapter=adapter;
        view.setAdapter(adapter);

        this.data=data;

        view.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }


    public void append(final ArrayList newData){

      //  Log.e("REBUILT???","BUEWHFUHF");

        Log.e("DATA","SIZE "+newData.size());

        int len = data.size();
        data.addAll(newData);
        //adapter.notifyItemRangeInserted(len,newData.size());

        adapter.notifyItemRangeInserted(len,newData.size());
       // adapter.notifyItemRangeChanged(0,len+newData.size());

      //  adapter.notifyItemRangeChanged(len,len+newData.size());

    }


    public void update(final ArrayList newData,int type){

        final DiffUtil.Callback  diffCallback;

        switch (type){
            case 0:
                diffCallback=new UserDiffCallback((ArrayList) data, newData);
                break;
            case 1:
                diffCallback=new PostDiffCallback((ArrayList) data, newData);
                break;
            default:
                diffCallback=null;
                    break;
        }




        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        data.clear();

        data.addAll(newData);


        diffResult.dispatchUpdatesTo(adapter);

    }



    public static class UserDiffCallback extends DiffUtil.Callback {

        private final ArrayList<User> oldUserList;
        private final ArrayList<User> newUserList;

        public UserDiffCallback(ArrayList<User> oldUserList, ArrayList<User>  newUserList) {
            this.oldUserList = oldUserList;
            this.newUserList = newUserList;

        }

        @Override
        public int getOldListSize() {
            return oldUserList.size();
        }

        @Override
        public int getNewListSize() {
            return newUserList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {


            final User oldUser = oldUserList.get(oldItemPosition);
            final User newUser = newUserList.get(newItemPosition);
            return (oldUser.getName().equals(newUser.getName())) && (oldUser.isMutual()==newUser.isMutual());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            final User oldUser = oldUserList.get(oldItemPosition);
            final User newUser = newUserList.get(newItemPosition);
            return (oldUser.getName().equals(newUser.getName())) && (oldUser.isMutual()==newUser.isMutual());
        }


        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            // Implement method if you're going to use ItemAnimator
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }

    }



    public static class PostDiffCallback extends DiffUtil.Callback {

        private final ArrayList<Post> oldPostList;
        private final ArrayList<Post> newPostList;

        public PostDiffCallback(ArrayList<Post> oldPostList, ArrayList<Post>  newPostList) {
            this.oldPostList = oldPostList;
            this.newPostList = newPostList;

        }

        @Override
        public int getOldListSize() {
            return oldPostList.size();
        }

        @Override
        public int getNewListSize() {
            return newPostList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {


            final Post oldPost = oldPostList.get(oldItemPosition);
            final Post newPost = newPostList.get(newItemPosition);
            return oldPost.getId().equals(newPost.getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            final Post oldPost = oldPostList.get(oldItemPosition);
            final Post newPost = newPostList.get(newItemPosition);
            return oldPost.getId().equals(newPost.getId());
        }


        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            // Implement method if you're going to use ItemAnimator
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }

    }


}



