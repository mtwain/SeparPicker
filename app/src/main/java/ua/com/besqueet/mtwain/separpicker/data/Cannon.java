package ua.com.besqueet.mtwain.separpicker.data;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by maxym_tarasyuk on 5/4/15.
 */
public class Cannon extends BasePoint {
    public String name;
    public double lat;
    public double lon;

    public Cannon(String name, LatLng position) {
        super(position);
        this.name = name;
        this.lat = position.latitude;
        this.lon = position.longitude;
    }

    public void setName(String name){this.name = name;}
    public String getName(){return this.name;}
    public void setPointposition(LatLng latLng){
        this.lat = latLng.latitude;
        this.lon = latLng.longitude;
    }
    public Bundle getPointposition(){
        Bundle bundle = new Bundle();
        bundle.putDouble("LATITUDE", markerLat);
        bundle.putDouble("LONGITUDE", markerLon);
        return bundle;
    }
}
