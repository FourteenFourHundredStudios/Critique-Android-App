package critique.fourteenfourhundred.marc.critique;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {


    public void login(String username,String password){

        JSONObject loginInfo = new JSONObject();
        try {
            loginInfo.put("username",username);
            loginInfo.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Util.postRequest(LoginActivity.this,Data.url+"login", loginInfo,
                new Response.Listener<JSONObject>(){
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("status").equals("error")) {
                                Util.showDialog(LoginActivity.this, response.getString("message"));
                            }else{
                                startApp(response.getString("apiKey"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Util.showDialog(LoginActivity.this, "error");
                    }
                }
        );
    }

    public void startApp(String apiKey){
        Data.apiKey=apiKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button button = findViewById(R.id.loginButton);
        final EditText usernameBox = findViewById(R.id.usernameBox);
        final EditText passwordBox = findViewById(R.id.passwordBox);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                login(usernameBox.getText().toString(),passwordBox.getText().toString());


            }
        });


    }
}
