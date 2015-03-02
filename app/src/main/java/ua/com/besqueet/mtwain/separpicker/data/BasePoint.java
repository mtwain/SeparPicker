package ua.com.besqueet.mtwain.separpicker.data;

import ua.com.besqueet.mtwain.separpicker.PointType;

public class BasePoint {

    public BasePoint(){};

    public BasePoint(PointType pointType, String pointName,double pointLat,double pointLon){
        type = pointType;
        name = pointName;
        lat = pointLat;
        lon = pointLon;
    }

    public double lat;
    public double lon;
    public String name;
    public PointType type;
}
