package com.fourteenfourhundred.critique.activities.HomeScreen.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.util.Util.Callback;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.fourteenfourhundred.critique.critique.R.layout.fragment_friends;


public class FriendsFragment extends Fragment {

    String mutuals[];
    View rootView;
    ArrayAdapter<String> adapter;
    ListView listView;
    boolean isEmpty=true;

    public FriendsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(fragment_friends, container, false);

        API.getMutuals(getActivity(),new Response.Listener<JSONObject>(){
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("status").equals("ok")){
                        JSONArray mutualNames = new JSONArray(response.get("message").toString());

                        mutuals= new String[mutualNames.length()];

                        for(int i=0;i<mutualNames.length();i++)mutuals[i]=mutualNames.get(i).toString();

                        listView = (ListView) rootView.findViewById(R.id.mutualsListFriends);
                        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(mutuals));
                        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1,lst);
                        listView.setAdapter(adapter);
                        listClickHandler();

                    }else{
                        Util.showDialog(getActivity(),response.get("message").toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        onType();




        return rootView;
    }

    public void listClickHandler(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,long id) {
                if(!isEmpty){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    follow(adapter.getItem(position));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to follow "+adapter.getItem(position)+"?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            }
        });
    }

    public void follow(String username){
        
    }

    public void updateList(JSONArray mutualNames){
        try {

            adapter.clear();

            for(int i=0;i<mutualNames.length();i++){
                //mutuals[i]=mutualNames.get(i).toString();
                adapter.add(mutualNames.get(i).toString());
            }

            adapter.notifyDataSetChanged();
            listView.deferNotifyDataSetChanged();



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onType(){
        EditText yourtext = (EditText)rootView.findViewById(R.id.mutualSearch);
        yourtext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Util.showDialog(getActivity(),s.toString());

                if(s.toString().trim().length()>0) {
                    isEmpty=false;
                    API.doSearch(getActivity(), s.toString().trim(), new Callback() {
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

        });
    }


}
