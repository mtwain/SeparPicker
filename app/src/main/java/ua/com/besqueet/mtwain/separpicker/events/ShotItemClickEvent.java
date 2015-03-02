package ua.com.besqueet.mtwain.separpicker.events;


import ua.com.besqueet.mtwain.separpicker.data.Shot;

public class ShotItemClickEvent {

    public Shot shot;

    public ShotItemClickEvent(Shot shot){
        this.shot = shot;
    }

}
