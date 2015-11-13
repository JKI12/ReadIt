package jake.king.sky.uk.cardview.Activity;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONObject;

import jake.king.sky.uk.cardview.Fragment.FragmentHandler;
import jake.king.sky.uk.cardview.R;
import jake.king.sky.uk.cardview.Utils.CallbackService;
import jake.king.sky.uk.cardview.Utils.JsonParser;
import jake.king.sky.uk.cardview.Utils.RequestUrlMaker;
import jake.king.sky.uk.cardview.Utils.SensitiveData;
import jake.king.sky.uk.cardview.Utils.VolleyHandler;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentHandler fragmentHandler;
    private RequestUrlMaker requestUrlMaker;
    private SensitiveData sd = new SensitiveData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentHandler = new FragmentHandler(fragmentManager);
        requestUrlMaker = new RequestUrlMaker();

        initWebView();

    }

    private void initWebView() {
        final WebView webView = (WebView) findViewById(R.id.redditRequest);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                System.out.println(url);
                System.out.println(sd.REDIRECT_URI);

                if (url.contains(sd.REDIRECT_URI)) {

                    System.out.println("--CALLED--");

                    //Gone to callbacks.html

                    fragmentHandler.showLoadingFragment(findViewById(R.id.main_wrapper));

                    

                    webView.destroy();

                }

            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//        webView.loadUrl(requestUrlMaker.getRequestString());
        webView.loadUrl("http://jki12.github.io/ReadIt/callback.html?error=test&code=jake&state=king");
    }
}
