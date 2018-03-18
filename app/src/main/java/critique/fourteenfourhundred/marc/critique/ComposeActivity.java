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

        getSupportActionBar().setCustomView(R.layout.compose_menu);

        getSupportActionBar().setDisplayShowTitleEnabled(false);



        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText e = findViewById(R.id.postTitle);
        e.requestFocus();
    }

    protected void onResume(){
        super.onResume();


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        //Util.showDialog(this,"here?");

        finish();

        return false;
    }

    public void createPost(){
        String content= ((EditText)findViewById(R.id.postContent)).getText().toString();
        String title= ((EditText)findViewById(R.id.postTitle)).getText().toString();
        Intent intent = new Intent(getApplicationContext(), SelectMutualsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        startActivityForResult(intent,0);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sendButton:
                createPost();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
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
