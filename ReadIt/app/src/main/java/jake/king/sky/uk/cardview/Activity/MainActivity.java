package jake.king.sky.uk.cardview.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import jake.king.sky.uk.cardview.Fragment.FragmentHandler;
import jake.king.sky.uk.cardview.R;
import jake.king.sky.uk.cardview.Utils.CallbackService;
import jake.king.sky.uk.cardview.Utils.RequestUrlMaker;
import jake.king.sky.uk.cardview.Utils.SensitiveData;
import jake.king.sky.uk.cardview.Utils.UrlParser;
import jake.king.sky.uk.cardview.Utils.VolleyHandler;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentHandler fragmentHandler;
    private RequestUrlMaker requestUrlMaker;
    private SensitiveData sd = new SensitiveData();
    private UrlParser up = new UrlParser();
    private VolleyHandler volleyHandler;

    Boolean loadingFinished = true;
    Boolean redirect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentHandler = new FragmentHandler(fragmentManager);
        requestUrlMaker = new RequestUrlMaker();
        volleyHandler = new VolleyHandler(getCacheDir());

        if(getString("access_token") != null)
            loadReadItView();
        else
            initWebView();

    }

    private void initWebView() {
        final WebView webView = (WebView) findViewById(R.id.redditRequest);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                if (!loadingFinished)
                {
                    redirect = true;
                }

                loadingFinished = false;

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                loadingFinished = false;
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                if (!redirect)
                {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect)
                {
                    System.out.println("LOADED");
                    getAuthToken(url);
                }
                else
                {
                    redirect = false;
                }
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(requestUrlMaker.getRequestString());

//        webView.loadUrl("http://jki12.github.io/ReadIt/callback.html?error=test&code=jake&state=king");

    }

    private void getAuthToken(String url){
        if (url.contains(sd.REDIRECT_URI) && !(url.contains("https://www.reddit.com/api/v1/authorize"))) {

            System.out.println("--Called--");

            WebView webView = (WebView) findViewById(R.id.redditRequest);
            webView.destroy();

            fragmentHandler.showLoadingFragment(findViewById(R.id.main_wrapper));

            //Gone to callbacks.html

            HashMap<String, String> query = new HashMap<String, String>();

            try {
                URL uURL = new URL(url);

                query = up.getQueryMap(uURL.getQuery());

            } catch (MalformedURLException e) {
                System.out.println(e);
            }

            //{error, state, code}

            System.out.println("QUERY: " + query);

            if (query != null) {
                if (query.get("error") == null || query.get("error").equals("")) {

                    System.out.println("no error");

                    CallbackService callbackService = new CallbackService() {
                        @Override
                        public void onSuccess(String response) {
                            fragmentHandler.closeLoadingFragment();
                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(response, JsonElement.class);
                            JsonObject jsonObject = element.getAsJsonObject();
                            saveResponseData(jsonObject);
                            loadReadItView();
                        }

                        @Override
                        public void onFailure(String response) {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    };

                    volleyHandler.postForAuthorisation(query.get("code"), sd.REDIRECT_URI, sd.CLIENT_ID, callbackService);

                } else {
                    switch (query.get("error")) {
                        case "access_denied":
                            fragmentHandler.closeLoadingFragment();
                            Toast.makeText(getApplicationContext(), "You Rejected Access, Closing App", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            } else {
                fragmentHandler.closeLoadingFragment();
                Toast.makeText(getApplicationContext(), "There has been an error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveResponseData(JsonObject jsonObject){
        saveString("access_token", jsonObject.get("access_token").toString());
        saveString("refresh_token", jsonObject.get("refresh_token").toString());
        saveString("token_type", jsonObject.get("token_type").toString());
        saveString("scope", jsonObject.get("scope").toString());
    }

    private void saveString(String name, String value){
        SharedPreferences sharedpreferences = getSharedPreferences("READIT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    private String getString(String name){
        SharedPreferences sharedpreferences = getSharedPreferences("READIT", Context.MODE_PRIVATE);
        return sharedpreferences.getString(name, null);
    }

    private void loadReadItView(){
        Intent intent = new Intent(this, ReaditViewActivity.class);
        startActivity(intent);
    }

}
