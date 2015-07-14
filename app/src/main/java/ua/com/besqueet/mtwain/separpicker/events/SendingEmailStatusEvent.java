package ua.com.besqueet.mtwain.separpicker.events;

/**
 * Created by mtwain on 13.07.2015.
 */
public class SendingEmailStatusEvent {

    public boolean isSuccessful;

    public SendingEmailStatusEvent(boolean b){
        isSuccessful = b;
    }
}
