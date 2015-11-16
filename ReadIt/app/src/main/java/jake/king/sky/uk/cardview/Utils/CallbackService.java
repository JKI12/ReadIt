package jake.king.sky.uk.cardview.Utils;

import com.android.volley.VolleyError;

public interface CallbackService {
    void onSuccess(String response);
    void onFailure(VolleyError response);
}
