package com.fourteenfourhundred.critique.UI.Fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.UI.Activities.HomeActivity;
import com.fourteenfourhundred.critique.UI.Views.RecycleViewManager;
import com.fourteenfourhundred.critique.UI.Views.UserAdapter;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class MutualsFragment extends Fragment {

    JSONObject mutuals[];
    ArrayAdapter<JSONObject> adapter;
    ListView listView;

    RecyclerView mutualsList;

    boolean isEmpty=true;
    public API api;

    public View rootView;

    RelativeLayout content;

    ProgressBar loading;

    public MutualsFragment(){

    }


    public int getLayout(){
        return R.layout.fragment_friends;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(getLayout(), container, false);

        content = rootView.findViewById(R.id.home_fragment_container);

        api=Data.backgroundApi;

        //setupListview();
        setupRecyclerView();




        return rootView;
    }

    public void setupRecyclerView(){

        try {


            mutualsList=(RecyclerView)rootView.findViewById(R.id.mutualsList);

            RecycleViewManager view = new RecycleViewManager(mutualsList, this.getActivity(),new UserAdapter(Data.getMutuals()));


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void setupListview() {




        try {

            mutuals = new JSONObject[Data.getMutuals().length()];


            for (int i = 0; i < Data.getMutuals().length(); i++) {
                mutuals[i] = new JSONObject(Data.getMutuals().get(i).toString());
            }

            listView = (ListView) rootView.findViewById(R.id.mutualsList);

            adapter = getListAdapter(new ArrayList<JSONObject>(Arrays.asList(mutuals)));
            listView.setAdapter(adapter);
            listClickHandler();


        }catch (Exception e){
            e.printStackTrace();
        }



       // onType();
    }

    public ArrayAdapter<JSONObject> getListAdapter(ArrayList<JSONObject> lst) {

        return new ArrayAdapter<JSONObject>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, lst){
            @Override
            public View getView(int position, View covertView, ViewGroup parent){
                View v=null;
                try {
                    v = LayoutInflater.from(rootView.getContext()).inflate(android.R.layout.simple_list_item_1, null);
                    TextView label = (TextView) v.findViewById(android.R.id.text1);
                    if(isEmpty) {

                        JSONObject user = new JSONObject(mutuals[position].toString());
                        label.setText(user.getString("username"));

                        if (user.getString("isMutual").equals("false")) {
                            label.setTextColor(Color.RED);
                            label.setText(user.getString("username") + " (not mutual)");
                        }
                    }else{
                        label.setText(adapter.getItem(position).getString("username"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return v;
            }
        };

    }

    public void listClickHandler(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,long id) {
                if(!isEmpty){
                    try {
                        final JSONObject user = new JSONObject(adapter.getItem(position).toString());
                        final String username=user.getString("username");
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        follow(username);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Do you want to follow "+username+"?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }catch (Exception e){

                    }

                }
            }
        });
    }

    public void follow(final String username){
        new ApiRequest.FollowRequest(api,username).execute(new Util.Callback(){
            public void onResponse(JSONObject obj){
                Util.showDialog(getActivity(),"You followed "+username+"!");
            }
        });
    }

    public void updateList(JSONArray mutualNames){
        try {

            adapter.clear();

            for(int i=0;i<mutualNames.length();i++){
                //mutuals[i]=new JSONObject(mutualNames.get(i).toString());
                adapter.add(new JSONObject(mutualNames.get(i).toString()).put("isMutual","true"));
                Log.e("hh",mutualNames.get(i).toString());
            }

            adapter.notifyDataSetChanged();
            listView.deferNotifyDataSetChanged();
            ((HomeActivity)getActivity()).stopLoadAnimation();


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /*
    public void onType(){
        EditText yourtext = (EditText)rootView.findViewById(R.id.mutualSearch);
        yourtext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if(s.toString().trim().length()>0) {
                    ((HomeActivity)getActivity()).startLoadAnimation();
                    isEmpty=false;
                    new ApiRequest.DoSearchRequest(api, s.toString().trim()).execute(new Util.Callback(){
                        public void onResponse(JSONObject object) {
                            try {
                                updateList(new JSONArray(object.getString("results")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    adapter.clear();

                    for(int i=0;i<mutuals.length;i++){
                        adapter.add(mutuals[i]);
                    }

                    adapter.notifyDataSetChanged();
                    listView.deferNotifyDataSetChanged();
                    isEmpty=true;
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Util.showDialog(getActivity(),s.toString());



            }

        });
    }
*/

}
