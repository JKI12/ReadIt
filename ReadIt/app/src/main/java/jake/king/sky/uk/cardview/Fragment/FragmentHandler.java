package jake.king.sky.uk.cardview.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class FragmentHandler {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public FragmentHandler(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    public void addFragment(Fragment fragment, String fName, int containerId) {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerId, fragment, fName);
        fragmentTransaction.commitAllowingStateLoss();

    }

    public void replaceFragment(Fragment fragment, String fName, int containerId) {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment, fName);
        fragmentTransaction.commitAllowingStateLoss();

    }

    public void closeFragment(String fName) {
        Fragment loadingFragment = fragmentManager.findFragmentByTag(fName);

        if(loadingFragment != null){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(loadingFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

}
