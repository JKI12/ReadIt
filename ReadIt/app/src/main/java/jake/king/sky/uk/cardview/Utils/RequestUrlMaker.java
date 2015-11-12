package jake.king.sky.uk.cardview.Utils;

import java.util.Random;

public class RequestUrlMaker {

    //https://www.reddit.com/api/v1/authorize?client_id=KKnonOdJqwiMmA&response_type=code&state=ddwedw&redirect_uri=http://jki12.github.io/ReadIt/&duration=permanent&scope=mysubreddits,identity

    private String requestString;
    private SensitiveData sd = new SensitiveData();

    public String getRequestString(){

        setRequestString();

        return requestString;
    }

    private void setRequestString(){
        String requestUrl = "https://www.reddit.com/api/v1/authorize.compact?";
        String client_id = "client_id=" + sd.CLIENT_ID;
        String response_type = "response_type=code";
        String state = "state=" + randomStringGen();
        String reditect_uri = "redirect_uri=" + sd.REDIRECT_URI;
        String duration = "duration=permanent";
        String scope = "scope=identity,mysubreddits";

        requestString = requestUrl + client_id + "&" + response_type + "&" + state + "&" + reditect_uri + "&" + duration + "&" + scope;

    }

    private String randomStringGen(){
        String randomString = "";
        Random rnd = new Random();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        int numberOfChars = rnd.nextInt(20);

        for (int i = 0; i < numberOfChars; i++) {
            int x = rnd.nextInt(alphabet.length);
            randomString += alphabet[x];
        }

        return randomString;

    }

}
