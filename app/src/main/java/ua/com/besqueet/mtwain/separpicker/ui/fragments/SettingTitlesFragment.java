package ua.com.besqueet.mtwain.separpicker.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;

public class SettingTitlesFragment extends Fragment {

    public SettingTitlesFragment(){}

    @InjectView(R.id.itemMain)
    TextView itemMain;
    @InjectView(R.id.itemSecurity)
    TextView itemSecurity;
    @InjectView(R.id.itemOptional)
    TextView itemOptional;

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
        View rootView = inflater.inflate(R.layout.fragment_setting_titles,container,false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @OnClick(R.id.itemMain) void onMainItemClick(){
        itemMain.setBackgroundResource(R.drawable.text_item_background_selected);
        itemOptional.setBackgroundResource(R.drawable.text_item_background);
        itemSecurity.setBackgroundResource(R.drawable.text_item_background);
        presentDetailFragment(new SettingMainFragment());
    }
    @OnClick(R.id.itemSecurity) void onSecurityItemClick(){
        itemMain.setBackgroundResource(R.drawable.text_item_background);
        itemOptional.setBackgroundResource(R.drawable.text_item_background);
        itemSecurity.setBackgroundResource(R.drawable.text_item_background_selected);
        presentDetailFragment(new SettingSecurityFragment());
    }
    @OnClick(R.id.itemOptional) void onOptionalItemClick(){
        itemMain.setBackgroundResource(R.drawable.text_item_background);
        itemOptional.setBackgroundResource(R.drawable.text_item_background_selected);
        itemSecurity.setBackgroundResource(R.drawable.text_item_background);
        presentDetailFragment(new SettingOptionalFragment());
    }

    public void presentDetailFragment(Fragment fragment){
        int container;

        if(UtilsController.INSTANCE.isTablet()){
            container = R.id.datailContainer;
            getFragmentManager().beginTransaction().replace(container,fragment)
            .commit();
        }else {
            container = R.id.container;
            getFragmentManager().beginTransaction().replace(container,fragment)
            .addToBackStack("")
            .commit();
        }
    }

}
