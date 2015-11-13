package jake.king.sky.uk.cardview.Utils;

import java.util.HashMap;
import java.util.Map;

public class UrlParser {

    public HashMap<String, String> getQueryMap(String query)
    {
        String[] params = query.split("&");
        HashMap<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

}
