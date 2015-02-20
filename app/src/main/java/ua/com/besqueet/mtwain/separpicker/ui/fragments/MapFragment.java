package ua.com.besqueet.mtwain.separpicker.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.halfbit.tinybus.Subscribe;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.ToolBarKeeper;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.controllers.VectorController;
import ua.com.besqueet.mtwain.separpicker.events.LoadVectorFinishEvent;

public class MapFragment extends Fragment implements ToolBarKeeper{

    public MapFragment(){}

    @InjectView(R.id.btnSettings)
    ImageView btnSettings;
    @InjectView(R.id.btnContacts)
    ImageView btnContacts;
    @InjectView(R.id.btnShots)
    ImageView btnShots;


    VectorController vectorInstance;
    BusController busInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vectorInstance = VectorController.INSTANCE;
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
        createView();
        return rootView;
    }

    @OnClick(R.id.btnSettings) void onSettingsBtnClick(){
        ContextController.INSTANCE.getMainActivity().presentFragmentAbove(new SettingsFragment());
    }
    @OnClick(R.id.btnShots) void onShotsBtnClick(){

    }
    @OnClick(R.id.btnContacts) void onContactsBtnClick(){
        ContextController.INSTANCE.getMainActivity().presentFragmentAbove(new ContactsFragment());
    }

    @Override
    public void createView(){
        btnShots.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        btnSettings.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        btnContacts.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        btnSettings.setImageDrawable(vectorInstance.settings);
        btnContacts.setImageDrawable(vectorInstance.users);
        btnShots.setImageDrawable(vectorInstance.shots);
    }

    @Subscribe
    public void onLoadVectorFinishEvent(LoadVectorFinishEvent event){
        createView();
    }

}
