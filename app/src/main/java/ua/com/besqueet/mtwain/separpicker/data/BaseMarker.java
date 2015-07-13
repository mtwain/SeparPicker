package ua.com.besqueet.mtwain.separpicker.data;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by maxym_tarasyuk on 5/4/15.
 */
public class BaseMarker {

    public String markerName;
    public String markerInfo;
    public MarkerType markerType;
    public String markerIcon;
    public double markerLat;
    public double markerLon;

    public BaseMarker() {
    }

    public BaseMarker(LatLng latLng) {
        this.markerLat = latLng.latitude;
        this.markerLon = latLng.longitude;
    }

    public void setMarkerName(String markerName){this.markerName = markerName;}
    public String getMarkerName(){return this.markerName;}
    public void setMarkerInfo(String markerInfo){this.markerInfo = markerInfo;}
    public String getMarkerInfo(){return this.markerInfo;}
    public void setMarkerType(MarkerType markerType){this.markerType = markerType;}
    public MarkerType getMarkerType(){return this.markerType;}
    public void setMarkerIcon(String markerIcon){this.markerIcon = markerIcon;}
    public String getMarkerIcon(){return this.markerIcon;}
    public void setMarkerPosition(double lat, double lon){this.markerLat = lat; this.markerLon = lon;}
    public Bundle getMarkerPosition() {
        Bundle bundle = new Bundle();
        bundle.putDouble("LATITUDE", markerLat);
        bundle.putDouble("LONGITUDE", markerLon);
        return bundle;
    }
}
