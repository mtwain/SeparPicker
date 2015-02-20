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
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.ToolBarKeeper;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;
import ua.com.besqueet.mtwain.separpicker.controllers.VectorController;

public class SettingsFragment extends Fragment implements ToolBarKeeper{

    public SettingsFragment(){}

    @InjectView(R.id.btnBack)
    ImageView btnBack;
    VectorController vectorInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vectorInstance = VectorController.INSTANCE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_settings,container,false);
        ButterKnife.inject(this, rootView);
        createView();
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


    @Override
    public void createView() {
        btnBack.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        btnBack.setImageDrawable(vectorInstance.back);
    }
}
