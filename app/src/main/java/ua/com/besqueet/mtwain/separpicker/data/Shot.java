package ua.com.besqueet.mtwain.separpicker.data;


import java.util.ArrayList;

public class Shot {

    public ShotType type;
    public Long id;
    public String name;
    public Long time;
    public ArrayList<BasePoint> points = new ArrayList<>();
    public String comments = "";

}
