package ua.com.besqueet.mtwain.separpicker.data;


import com.google.android.gms.maps.model.CameraPosition;

import java.util.ArrayList;

public class Shot {

    public ShotType type;
    public Long id;
    public String name;
    public String time;
    public ArrayList<BaseMarker> points = new ArrayList<>();
    public String comments = "";

}
