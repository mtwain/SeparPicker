package ua.com.besqueet.mtwain.separpicker.ui.fragments.shots;


import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.separpicker.PointType;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ShotsController;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;
import ua.com.besqueet.mtwain.separpicker.data.BasePoint;
import ua.com.besqueet.mtwain.separpicker.data.OutShot;
import ua.com.besqueet.mtwain.separpicker.data.ShotType;

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

    @OnClick(R.id.btnAdd) void onAddBtnClick(){
        OutShot shot = new OutShot();
        Random random = new Random();
        Long id = System.currentTimeMillis();
        Double lat = random.nextDouble();
        Double lon = random.nextDouble();
        Location location1 = new Location("My position 1");
        location1.setLongitude(lon);
        location1.setLatitude(lat);
        BasePoint firstPoint = new BasePoint(PointType.MY_POSITION,"My position 1",lat,lon);

        lat = random.nextDouble();
        lon = random.nextDouble();
        Location location2 = new Location("My position 2");
        location2.setLatitude(lat);
        location2.setLongitude(lon);
        BasePoint secondPoint = new BasePoint(PointType.MY_POSITION,"My position 2",lat,lon);

        ArrayList<BasePoint> points = new ArrayList<>();
        points.add(firstPoint);
        points.add(secondPoint);

        shot.points = points;
        shot.time = id;
        shot.id = id;
        shot.name = "SHot "+id;
        shot.type = ShotType.OUT;

        ShotsController.INSTANCE.addShot(shot);
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
