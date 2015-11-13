package jake.king.sky.uk.cardview.Utils;

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

import java.io.File;

/**
 * Created by jki12 on 13/11/2015.
 */
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
                    callbackService.onFailure(error.getLocalizedMessage());
                }
            });

        requestQueue.add(stringRequest);
    }

}
