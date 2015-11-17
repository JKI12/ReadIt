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

    public void showLoadingFragment(View layout){

        Fragment loadingFragment = fragmentManager.findFragmentByTag("loadingFragment");

        if(loadingFragment == null){
            loadingFragment = new LoadingFragment();

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(layout.getId(), loadingFragment, "loadingFragment");
            fragmentTransaction.commit();
        }

    }

    public void closeLoadingFragment(){
        Fragment loadingFragment = fragmentManager.findFragmentByTag("loadingFragment");

        if(loadingFragment != null){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(loadingFragment);
            fragmentTransaction.commit();
        }

    }

}
