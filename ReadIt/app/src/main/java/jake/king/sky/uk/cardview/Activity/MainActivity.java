package jake.king.sky.uk.cardview.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jake.king.sky.uk.cardview.Fragment.FragmentHandler;
import jake.king.sky.uk.cardview.R;

//https://www.reddit.com/api/v1/authorize?client_id=KKnonOdJqwiMmA&response_type=code&state=ddwedw&redirect_uri=http://jki12.github.io/ReadIt/&duration=permanent&scope=mysubreddits

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentHandler fragmentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentHandler = new FragmentHandler(fragmentManager);

    }
}
