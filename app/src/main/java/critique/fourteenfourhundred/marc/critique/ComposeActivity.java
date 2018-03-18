package critique.fourteenfourhundred.marc.critique;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

public class ComposeActivity extends AppCompatActivity {

    //rename to activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        /*
        SearchView s = findViewById(R.id.postTitle);
        s.setQueryHint("Post title");*/

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setCustomView(R.layout.compose_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_button);


        EditText e = findViewById(R.id.postTitle);
        e.requestFocus();
    }

    protected void onResume(){
        super.onResume();


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up

        return false;
    }

    public void createPost(){
        String content= ((EditText)findViewById(R.id.postContent)).getText().toString();
        String title= ((EditText)findViewById(R.id.postTitle)).getText().toString();
        Intent intent = new Intent(getApplicationContext(), SelectMutualsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sendButton:
                createPost();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.vote_menu,menu);

       // android.app.ActionBar actionBar = getActionBar();
        //actionBar.setCustomView(R.layout.compose_menu);



        return super.onCreateOptionsMenu(menu);
    }

}
