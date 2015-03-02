package ua.com.besqueet.mtwain.separpicker.ui.fragments.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;

public class SettingsFragment extends Fragment{

    public SettingsFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_settings,container,false);
        ButterKnife.inject(this, rootView);
            getFragmentManager().beginTransaction()
                    .add(R.id.titlesContainer, new SettingTitlesFragment())
                    .commit();
            if(UtilsController.INSTANCE.isTablet()){
                getFragmentManager().beginTransaction()
                        .add(R.id.datailContainer,new SettingMainFragment())
                        .commit();
            }

        return rootView;
    }

    @OnClick(R.id.btnBack) void onBackBtnClick(){
        getFragmentManager().popBackStack();
    }


}
