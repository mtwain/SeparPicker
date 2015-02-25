package ua.com.besqueet.mtwain.separpicker.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import de.halfbit.tinybus.Subscribe;
import de.halfbit.tinybus.TinyBus;
import de.halfbit.tinybus.wires.ConnectivityWire;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContactsController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;
import ua.com.besqueet.mtwain.separpicker.controllers.VectorController;
import ua.com.besqueet.mtwain.separpicker.ui.fragments.MapFragment;


public class MainActivity extends Activity {

    boolean isTablet;
    ContextController contextInstance;
    BusController busInstance;
    UtilsController utilsInstance;
    VectorController vectorInstance;
    ContactsController contactsInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        busInstance = BusController.INSTANCE;
        contextInstance = ContextController.INSTANCE;
        utilsInstance = UtilsController.INSTANCE;
        vectorInstance = VectorController.INSTANCE;
        contactsInstance = ContactsController.INSTANCE;

        isTablet = getResources().getBoolean(R.bool.isTablet);
        utilsInstance.setTablet(isTablet);

        if (isTablet) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        contextInstance.setMainActivity(this);
        busInstance.setBus(TinyBus.from(this));
        vectorInstance.loadVectors(this);
        contactsInstance.initContactsDB(this);

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
                .replace(R.id.container,fragment)
                .commit();
    }

    public void presentFragmentAbove(Fragment fragment){
        getFragmentManager().beginTransaction()
                .replace(R.id.container,fragment)
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
