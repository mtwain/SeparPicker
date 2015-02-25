package ua.com.besqueet.mtwain.separpicker.data;

import android.location.Location;

import ua.com.besqueet.mtwain.separpicker.ShotType;

public class BasePoint {

    public BasePoint(ShotType pointType, String pointName, Location pointLocation){
        type = pointType;
        name = pointName;
        location = pointLocation;
    }

    public String name;
    public Location location;
    public ShotType type;
}
