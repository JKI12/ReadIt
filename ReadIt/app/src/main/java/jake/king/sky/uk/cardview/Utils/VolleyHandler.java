package jake.king.sky.uk.cardview.Utils;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class VolleyHandler {

    RequestQueue requestQueue;
    Cache cache;
    Network network;

    public VolleyHandler(File cacheDirectory){
        cache = new DiskBasedCache(cacheDirectory, 1024 * 1024);
        network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);

        requestQueue.start();
    }

    public void getRequest(String url, final CallbackService callbackService){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Do something with the response
                    callbackService.onSuccess(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error
                    callbackService.onFailure(error);
                }
            });

        requestQueue.add(stringRequest);
    }

    public void postForAuthorisation(String code, String redirectUri, String clientID, final CallbackService callbacks){

        final String aCode = code;
        final String aRedirectUri = redirectUri;
        final String aClientID = clientID;

        StringRequest sr = new StringRequest(Request.Method.POST, "https://www.reddit.com/api/v1/access_token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callbacks.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callbacks.onFailure(error);
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();

                params.put("code", aCode);
                params.put("redirect_uri", aRedirectUri);
                params.put("grant_type", "authorization_code");

                return params;
            }
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Basic "
                        + Base64.encodeToString((aClientID).getBytes(),
                        Base64.DEFAULT).replace("=", "6");

                headers.put("Authorization", auth);
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(sr);
    }

    public void refreshToken(final String refreshToken, final CallbackService callbacks, final String clientID){

        System.out.println("REFRESH: " + refreshToken);

        StringRequest sr = new StringRequest(Request.Method.POST, "https://www.reddit.com/api/v1/access_token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callbacks.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callbacks.onFailure(error);
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                String formatToken = refreshToken.replace("\"", "");
                params.put("grant_type", "refresh_token");
                params.put("refresh_token", formatToken);

                return params;
            }
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Basic "
                        + Base64.encodeToString((clientID).getBytes(),
                        Base64.DEFAULT).replace("=", "6");

                headers.put("Authorization", auth);
                return headers;
            }
        };

        requestQueue.add(sr);

    }

    public void getUsersInfo(String token, String userAgent, final CallbackService callbacks){

        final String aUserAgent = userAgent;
        final String aToken = token;

        StringRequest getInfo = new StringRequest(Request.Method.GET, "https://oauth.reddit.com/api/v1/me", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callbacks.onSuccess(response);
            }
        },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callbacks.onFailure(error);
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                String auth = "bearer " + aToken.replace("\"", "");
                params.put("Authorization", auth);
                params.put("User-Agent", aUserAgent);

                return params;
            }
        };

        requestQueue.add(getInfo);

    }

    public void getUsersSubs(String token, String userAgent, final CallbackService callbacks){

        final String aUserAgent = userAgent;
        final String aToken = token;

        StringRequest getInfo = new StringRequest(Request.Method.GET, "https://oauth.reddit.com/subreddits/mine/subscriber", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callbacks.onSuccess(response);
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callbacks.onFailure(error);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                String auth = "bearer " + aToken.replace("\"", "");
                params.put("Authorization", auth);
                params.put("User-Agent", aUserAgent);

                return params;
            }
        };

        requestQueue.add(getInfo);
    }

}
