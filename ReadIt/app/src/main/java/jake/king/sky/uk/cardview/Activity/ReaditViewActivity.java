package jake.king.sky.uk.cardview.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jake.king.sky.uk.cardview.Fragment.FragmentHandler;
import jake.king.sky.uk.cardview.R;
import jake.king.sky.uk.cardview.Utils.CallbackService;
import jake.king.sky.uk.cardview.Utils.SensitiveData;
import jake.king.sky.uk.cardview.Utils.StringFormatter;
import jake.king.sky.uk.cardview.Utils.VolleyHandler;

public class ReaditViewActivity extends AppCompatActivity {

    private VolleyHandler volleyHandler;
    private SensitiveData sd = new SensitiveData();
    private StringFormatter sf = new StringFormatter();
    private FragmentHandler fragmentHandler;

    private Gson gson = new Gson();

    private boolean refreshedToken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);

        setContentView(R.layout.activity_readitview);

        volleyHandler = new VolleyHandler(getCacheDir());
        fragmentHandler = new FragmentHandler(getSupportFragmentManager());

        getUsersInfo();
        getUsersSubs();

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private String getString(String name){
        SharedPreferences sharedpreferences = getSharedPreferences("READIT", Context.MODE_PRIVATE);
        return sharedpreferences.getString(name, null);
    }

    private void saveString(String name, String value){
        SharedPreferences sharedpreferences = getSharedPreferences("READIT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    private void getUsersInfo(){
        final TextView textView = (TextView) findViewById(R.id.readitview_name);

        CallbackService callbackService = new CallbackService() {
            @Override
            public void onSuccess(String response) {
                JsonElement element = gson.fromJson(response, JsonElement.class);
                JsonObject jsonObject = element.getAsJsonObject();

                textView.setText("Welcome " + sf.formatName(jsonObject.get("name").toString()));

            }
            @Override
            public void onFailure(VolleyError error) {

                if(error instanceof AuthFailureError){
                    if(!refreshedToken) {
                        refreshToken();
                    }
                }else {
                    System.out.println(error.toString());
                }
            }
        };

        volleyHandler.getUsersInfo(getString("access_token"), sd.USER_AGENT, callbackService);

    }

    private void getUsersSubs(){

        CallbackService callbackService = new CallbackService() {
            @Override
            public void onSuccess(String response) {
                JsonElement element = gson.fromJson(response, JsonElement.class);
                JsonObject jsonObject = element.getAsJsonObject();
            }
            @Override
            public void onFailure(VolleyError error) {

                if(error instanceof AuthFailureError){
                    if(!refreshedToken) {
                        refreshToken();
                    }
                }else {
                    System.out.println(error.toString());
                }
            }
        };

        volleyHandler.getUsersSubs(getString("access_token"), sd.USER_AGENT, callbackService);
    }

    private void refreshToken(){

        CallbackService callbacks = new CallbackService() {
            @Override
            public void onSuccess(String response) {

                fragmentHandler.closeLoadingFragment();

                Toast.makeText(getApplicationContext(), "Token Refreshed!", Toast.LENGTH_SHORT).show();

                Gson gson = new Gson();
                JsonElement element = gson.fromJson(response, JsonElement.class);
                JsonObject jsonObject = element.getAsJsonObject();

                refreshedToken = true;

                saveString("access_token", jsonObject.get("access_token").toString());

                finish();
                startActivity(getIntent());

            }

            @Override
            public void onFailure(VolleyError response) {
                System.out.println("REFRESH ERROR:" + response.toString());
            }
        };

        if(getString("refresh_token") != null){
            volleyHandler.refreshToken(getString("refresh_token"), callbacks, sd.CLIENT_ID);
            fragmentHandler.showLoadingFragment(findViewById(R.id.readitview_wrapper));
        }else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            saveString("access_token", null);
            finish();
            startActivity(intent);
        }
    }
}
