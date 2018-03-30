package critique.fourteenfourhundred.marc.critique;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by Marc on 27/3/18.
 */

public class QueView extends View{

    public JSONObject post;
    public View rootView;
    public Bitmap patch;

    public QueView(Context context,String post) {
        super(context);

        try {
            this.post = new JSONObject(post);
            //rootView = View.inflate(context, R.layout.fragment_que, null);

            LayoutInflater inflater = LayoutInflater.from(context);
            rootView= inflater.inflate(R.layout.fragment_que, null, false);

            init();
        }catch (Exception e){
            e.printStackTrace();
        }
        //rootView = inflate(context, R.layout.fragment_que, this);
    }


//    public void g


    public JSONObject getPost(){
        return post;
    }


    public View getSelf(){
        return rootView;
    }

    public void setPost(String post){
        try {
            Log.e("f","DID ITT");

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void init(){
        try {
            
            ((TextView) rootView.findViewById(R.id.postTitle)).setText(post.getString("title"));
            ((TextView) rootView.findViewById(R.id.postContent)).setText(post.getString("content"));
            ((TextView) rootView.findViewById(R.id.postSender)).setText(post.getString("username"));
            ((TextView) rootView.findViewById(R.id.postVoteCount)).setText(post.getString("votes") + " votes");

            API.getPatch((Activity)getContext(), post.getString("username"), new Callback() {
                public void onResponse(Bitmap img) {
                    ((ImageView) rootView.findViewById(R.id.queProfilePic)).setImageBitmap(img);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
