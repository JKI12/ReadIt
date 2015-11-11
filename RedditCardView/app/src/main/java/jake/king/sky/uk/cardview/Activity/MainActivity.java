package jake.king.sky.uk.cardview.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jake.king.sky.uk.cardview.Fragment.FragmentHandler;
import jake.king.sky.uk.cardview.R;

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