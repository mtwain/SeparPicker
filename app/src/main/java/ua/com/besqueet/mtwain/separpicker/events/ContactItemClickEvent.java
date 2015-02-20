package ua.com.besqueet.mtwain.separpicker.events;


import ua.com.besqueet.mtwain.separpicker.data.Contact;

public class ContactItemClickEvent {

    public Contact contact;

    public ContactItemClickEvent(Contact contact){
        this.contact = contact;
    }

}
