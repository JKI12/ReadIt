package jake.king.sky.uk.cardview.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jki12 on 13/11/2015.
 */
public class JsonParser {

    public JSONObject getJsonObject(String jsonString){

        JSONObject jsonObject = new JSONObject();

        try{
            JSONArray jsonArray = new JSONArray(jsonString);

            for(int i=0; i< jsonArray.length(); i++){
                jsonObject  = jsonArray.getJSONObject(i);
            }

        }catch (JSONException e){
            System.out.println(e);
        }

        return jsonObject;

    }

    public JSONObject get(JSONObject jsonObject, String dataName){

        JSONObject element = new JSONObject();

        try{
             element = (JSONObject) jsonObject.get(dataName);
        }catch (JSONException e){
            System.out.println(e);
        }

        return element;

    }

}
