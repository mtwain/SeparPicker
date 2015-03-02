package ua.com.besqueet.mtwain.separpicker.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.separpicker.FragmentAnimationDirection;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.ui.fragments.contacts.ContactsFragment;
import ua.com.besqueet.mtwain.separpicker.ui.fragments.settings.SettingsFragment;
import ua.com.besqueet.mtwain.separpicker.ui.fragments.shots.ShotsFragment;

public class MapFragment extends Fragment{

    public MapFragment(){}

    BusController busInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busInstance = BusController.INSTANCE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        busInstance.getBus().register(this);
    }

    @Override
    public void onStop() {
        busInstance.getBus().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map,container,false);
        ButterKnife.inject(this,rootView);
        return rootView;
    }

    @OnClick(R.id.btnSettings) void onSettingsBtnClick(){
        ContextController.INSTANCE.getMainActivity().presentFragmentAbove(new SettingsFragment(),FragmentAnimationDirection.FROM_LEFT);
    }
    @OnClick(R.id.btnShots) void onShotsBtnClick(){
        ContextController.INSTANCE.getMainActivity().presentFragmentAbove(new ShotsFragment(), FragmentAnimationDirection.FROM_LEFT);
    }
    @OnClick(R.id.btnContacts) void onContactsBtnClick(){
        ContextController.INSTANCE.getMainActivity().presentFragmentAbove(new ContactsFragment(), FragmentAnimationDirection.FROM_LEFT);
    }

}
