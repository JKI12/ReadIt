package jake.king.sky.uk.cardview.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import jake.king.sky.uk.cardview.Fragment.FragmentHandler;
import jake.king.sky.uk.cardview.Fragment.LoadingFragment;
import jake.king.sky.uk.cardview.Fragment.PostsFragment;
import jake.king.sky.uk.cardview.Models.CardInfo;
import jake.king.sky.uk.cardview.R;
import jake.king.sky.uk.cardview.Utils.CallbackService;
import jake.king.sky.uk.cardview.Utils.SensitiveData;
import jake.king.sky.uk.cardview.Utils.StringFormatter;
import jake.king.sky.uk.cardview.Utils.VolleyHandler;

public class ReaditViewActivity extends AppCompatActivity {

    private VolleyHandler volleyHandler;
    private SensitiveData sd = new SensitiveData();
    private FragmentHandler fragmentHandler;

    private Gson gson = new Gson();

    private boolean refreshedToken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        volleyHandler.getUsersInfo(getString("access_token"), sd.USER_AGENT, callbackService);

    }

    private void getUsersSubs(){

        CallbackService callbackService = new CallbackService() {
            @Override
            public void onSuccess(String response) {

                ArrayList<String[]> subreddits = new ArrayList<String[]>();

                JsonElement element = gson.fromJson(response, JsonElement.class);
                JsonObject jsonObject = element.getAsJsonObject();

                JsonObject data = jsonObject.get("data").getAsJsonObject();
                JsonArray children = data.get("children").getAsJsonArray();

                for(JsonElement child : children) {
                    JsonObject subreddit = child.getAsJsonObject();
                    JsonObject subData = subreddit.get("data").getAsJsonObject();
                    String[] info = {subData.get("title").toString(), subData.get("url").toString()};
                    subreddits.add(info);
                }

                //changeFragment(subreddits);

                getPosts(subreddits);

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

                fragmentHandler.closeFragment("loading");

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
            fragmentHandler.addFragment(new LoadingFragment(), "loading", R.id.readitview_wrapper);
        }else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            saveString("access_token", null);
            finish();
            startActivity(intent);
        }
    }

    private void getPosts(ArrayList<String[]> subreddits) {

        ArrayList<CardInfo> posts = new ArrayList<CardInfo>();

        CardInfo mockPost = new CardInfo("Mock post 1", "Votes: 20", BitmapFactory.decodeResource(getResources(), R.drawable.self), null, true);
        CardInfo mockPost1 = new CardInfo("Mock post 2", "Votes: 23", BitmapFactory.decodeResource(getResources(), R.drawable.self), null, false);



        posts.add(mockPost); posts.add(mockPost1);

        changeFragment(posts);

    }

    private void changeFragment(ArrayList<CardInfo> posts) {
        Fragment postFragment = new PostsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("SUBS", posts);

        postFragment.setArguments(bundle);

        fragmentHandler.replaceFragment(postFragment, "posts", R.id.readitview_wrapper);
    }

}
