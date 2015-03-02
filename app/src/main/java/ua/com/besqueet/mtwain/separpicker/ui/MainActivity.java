package ua.com.besqueet.mtwain.separpicker.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import de.halfbit.tinybus.Subscribe;
import de.halfbit.tinybus.TinyBus;
import de.halfbit.tinybus.wires.ConnectivityWire;
import ua.com.besqueet.mtwain.separpicker.FragmentAnimationDirection;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContactsController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.controllers.ShotsController;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;
import ua.com.besqueet.mtwain.separpicker.ui.fragments.MapFragment;


public class MainActivity extends Activity {

    boolean isTablet;
    ContextController contextInstance;
    BusController busInstance;
    UtilsController utilsInstance;
    ContactsController contactsInstance;
    ShotsController shotsInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        busInstance = BusController.INSTANCE;
        contextInstance = ContextController.INSTANCE;
        utilsInstance = UtilsController.INSTANCE;
        contactsInstance = ContactsController.INSTANCE;
        shotsInstance = ShotsController.INSTANCE;

        isTablet = getResources().getBoolean(R.bool.isTablet);
        utilsInstance.setTablet(isTablet);

        if (isTablet) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        contextInstance.setMainActivity(this);
        busInstance.setBus(TinyBus.from(this));
        contactsInstance.initDBforContacts(this);
        shotsInstance.initDBforShot(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presentFragment(new MapFragment());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!busInstance.getBus().hasRegistered(this)){
            busInstance.getBus().register(this);
        }

    }

    @Override
    protected void onStop() {
        if (busInstance.getBus().hasRegistered(this)) {
            busInstance.getBus().unregister(this);
        }
        super.onStop();
    }

    @Subscribe
    public void onConnectivityEvent(ConnectivityWire.ConnectionStateEvent event) {
        if (event.isConnected()) {
            //Toast.makeText(this,"Connected to network",Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(this,"Disconnected",Toast.LENGTH_SHORT).show();
        }
    }

    public void presentFragment(Fragment fragment){
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_from_bottom,
                        R.animator.slide_out_to_top,
                        R.animator.slide_in_from_bottom,
                        R.animator.slide_out_to_top)
                .replace(R.id.container, fragment)
                .commit();
    }

    public void presentFragmentAbove(Fragment fragment,FragmentAnimationDirection side){

        int first_in=0;
        int second_out=0;
        int second_in=0;
        int first_out=0;

        if(FragmentAnimationDirection.FROM_LEFT.equals(side)){
            first_in = R.animator.slide_in_from_left;
            second_out = R.animator.slide_out_to_right;
            second_in = R.animator.slide_in_from_right;
            first_out = R.animator.slide_out_to_left;
        }else if(FragmentAnimationDirection.FROM_RIGHT.equals(side)){
            first_in = R.animator.slide_in_from_right;
            second_out = R.animator.slide_out_to_left;
            second_in = R.animator.slide_in_from_left;
            first_out = R.animator.slide_out_to_right;
        }else if(FragmentAnimationDirection.FROM_BOTTOM.equals(side)){
            first_in = R.animator.slide_in_from_bottom;
            second_out = R.animator.slide_out_to_top;
            second_in = R.animator.slide_in_from_top;
            first_out = R.animator.slide_out_to_bottom;
        }


        getFragmentManager().beginTransaction()
                .setCustomAnimations(
                    first_in,
                    second_out,
                    second_in,
                    first_out
                )
                .replace(R.id.container, fragment)
                .addToBackStack("")
                .commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
        presentFragment(new MapFragment());
    }

}
