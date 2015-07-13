package ua.com.besqueet.mtwain.separpicker.ui.fragments.shots;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;

public class ShotsFragment extends Fragment {

    public ShotsFragment(){}

    @InjectView(R.id.barTitle)
    TextView barTitle;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shots,container,false);
        ButterKnife.inject(this, rootView);
        Log.d("aaa","oncvsf");
        getFragmentManager().beginTransaction()
                .add(R.id.containerContacts, new ShotsListFragment())
                .commit();
        if(UtilsController.INSTANCE.isTablet()){
            getFragmentManager().beginTransaction()
                    .add(R.id.containerDetail, new ShotDetailFragment())
                    .commit();
        }
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(!UtilsController.INSTANCE.isTablet()&&getFragmentManager()!=null){
                    int count = getFragmentManager().getBackStackEntryCount();
                    Log.d("L", "Count: " + count);
                    if(count==2){
                        barTitle.setText("Шот");
                    }else if(count==1){
                        barTitle.setText("Шоти");
                    }
                }
            }
        });
        return rootView;
    }


    @OnClick(R.id.btnBack) void onBackBtnClick(){
        getActivity().getFragmentManager().popBackStack();
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

}
