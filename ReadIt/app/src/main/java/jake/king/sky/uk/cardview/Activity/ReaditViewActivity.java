package jake.king.sky.uk.cardview.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jake.king.sky.uk.cardview.R;
import jake.king.sky.uk.cardview.Utils.CallbackService;
import jake.king.sky.uk.cardview.Utils.SensitiveData;
import jake.king.sky.uk.cardview.Utils.StringFormatter;
import jake.king.sky.uk.cardview.Utils.VolleyHandler;

public class ReaditViewActivity extends AppCompatActivity {

    VolleyHandler volleyHandler;
    SensitiveData sd = new SensitiveData();
    StringFormatter sf = new StringFormatter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readitview);

        volleyHandler = new VolleyHandler(getCacheDir());

        getUsersInfo();
        getUsersSubs();

    }

    private String getString(String name){
        SharedPreferences sharedpreferences = getSharedPreferences("READIT", Context.MODE_PRIVATE);
        return sharedpreferences.getString(name, null);
    }

    private void getUsersInfo(){
        final TextView textView = (TextView) findViewById(R.id.readitview_name);

        CallbackService callbackService = new CallbackService() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                JsonElement element = gson.fromJson(response, JsonElement.class);
                JsonObject jsonObject = element.getAsJsonObject();

                textView.setText("Welcome " + sf.formatName(jsonObject.get("name").toString()));

            }

            @Override
            public void onFailure(String response) {
                System.out.println("ERROR: " + response);
            }
        };

        volleyHandler.getUsersInfo(getString("access_token"), sd.USER_AGENT, callbackService);

    }

    private void getUsersSubs(){

        CallbackService callbackService = new CallbackService() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                JsonElement element = gson.fromJson(response, JsonElement.class);
                JsonObject jsonObject = element.getAsJsonObject();

                System.out.println(jsonObject);

            }

            @Override
            public void onFailure(String response) {
                System.out.println("ERROR: " + response);
            }
        };

        volleyHandler.getUsersSubs(getString("access_token"), sd.USER_AGENT, callbackService);

    }

}
