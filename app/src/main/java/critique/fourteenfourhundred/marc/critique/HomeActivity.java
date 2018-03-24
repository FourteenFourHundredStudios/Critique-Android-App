package critique.fourteenfourhundred.marc.critique;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {


    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.pager);


        viewPager.setOffscreenPageLimit(5);


        // Create an adapter that knows which fragment should be shown on each page
        HomePageAdapter adapter = new HomePageAdapter(this, getSupportFragmentManager());



        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);


        //tabLayout.setSelectedTabIndicatorHeight(0);






    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.home_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openCompose:


                Intent intent = new Intent(getApplicationContext(), ComposeActivity.class);
                startActivity(intent);
                //viewPager.setCurrentItem(1, true);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void selectPatch(View view) {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if (resultCode != RESULT_OK) return;

        switch (requestCode) {

            case 1:
                if (data != null) {


                    Uri path = data.getData();

                    try {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                        API.changePatch(HomeActivity.this,bitmap,new Callback(){
                            public void onResponse(JSONObject response){
                                try {
                                    if (response.get("status").equals("ok")) {
                                        setProfilePatch(bitmap);
                                    }else{
                                        Util.showDialog(HomeActivity.this,response.getString("message"));
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }

    }


    public void setProfilePatch(Bitmap img){

        ImageView f = (ImageView) findViewById(R.id.userPatch);
        f.setImageBitmap(img);
    }






}


