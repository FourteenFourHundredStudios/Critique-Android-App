package com.fourteenfourhundred.critique.UI.Views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONArray;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private JSONArray users;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView username;
        public TextView points;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.username = view.findViewById(R.id.username);
            this.points = view.findViewById(R.id.points);
        }
    }


    public UserAdapter(JSONArray users) {
        this.users = users;
    }


    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_user_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {
            holder.username.setText(users.getJSONObject(position).getString("username"));
            holder.points.setText(users.getJSONObject(position).getString("points"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return users.length();
    }

}