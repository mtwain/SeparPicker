package ua.com.besqueet.mtwain.separpicker.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.controllers.ShotsController;
import ua.com.besqueet.mtwain.separpicker.data.BaseMarker;

/**
 * Created by maxym_tarasyuk on 5/15/15.
 */
public class MapReviewFragment extends Fragment implements Constants {

    BusController busInstance;

    GoogleMap map;
    BaseMarker myMapMarker;
    ArrayList<BaseMarker> markerList;
    MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busInstance = BusController.INSTANCE;
        MapsInitializer.initialize(ContextController.INSTANCE.getMainActivity());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*View rootView = inflater.inflate(R.layout.fragment_map_review,container,false);
        ButterKnife.inject(this, rootView);
        mapView = (MapView) rootView.findViewById(R.id.mapviewreview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        if(map == null){
            Log.d("aaa","MAPNULL1");
        }*/
        View v = inflater.inflate(R.layout.fragment_map_review, null, false);
        mapView = (MapView) v.findViewById(R.id.mapviewreview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();

        String list = getArguments().getString(MARKER_LIST);//TODO ?
        /*if(!list.equals("")){
            Gson gson1 = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(list).getAsJsonArray();
            markerList = new ArrayList<>();
            for(int i=0; i<array.size(); i++){
                BaseMarker bm = gson1.fromJson(array.get(i), BaseMarker.class);
                markerList.add(bm);
                Log.d("aaa","POS1="+markerList.get(i).getMarkerPosition());
            }
        }*/
        markerList = ShotsController.INSTANCE.deserialize(list);
        Log.d("aaa", "+++4="+markerList.size());
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("aaa", "onViewCreated");
        for(int i=0;i<markerList.size();i++){//TODO ?
            myMapMarker = markerList.get(i);
            String info = myMapMarker.getMarkerInfo();
            Bundle bundle = myMapMarker.getMarkerPosition();
            double lat = bundle.getDouble("LATITUDE");
            double lon = bundle.getDouble("LONGITUDE");
            LatLng position = new LatLng(lat,lon);
            String name = myMapMarker.getMarkerName();
            String icon = myMapMarker.getMarkerIcon();

            MarkerOptions user_options = new MarkerOptions()
                    .position(position)
                    .title("Точка: " + name + "   Опис:" + info)
                    .icon(BitmapDescriptorFactory.fromResource(ShotsController.INSTANCE.manageMarkerIcon(icon)));
            map.addMarker(user_options);
        }
            //TODO встановити камеру
    }
}
