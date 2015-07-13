package ua.com.besqueet.mtwain.separpicker.data;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class BasePoint extends BaseMarker {

    public PointType pointType;
    public double pointLat;
    public double pointLon;

    public BasePoint(){}

    public BasePoint(LatLng latLng) {
        super(latLng);
        this.pointLat = latLng.latitude;
        this.pointLon = latLng.longitude;
    }

    public Bundle getPointposition() {
        Bundle bundle = new Bundle();
        bundle.putDouble("LATITUDE", markerLat);
        bundle.putDouble("LONGITUDE", markerLon);
        return bundle;
    }
    public void setPointposition(LatLng location) {this.pointLat = location.latitude; this.pointLon = location.longitude;}
    public PointType getType() {return pointType;}
    public void setType(PointType pointType) {this.pointType = pointType;}
}
