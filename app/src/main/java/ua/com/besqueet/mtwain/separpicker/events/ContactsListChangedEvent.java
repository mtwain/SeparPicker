package ua.com.besqueet.mtwain.separpicker.events;

import java.util.ArrayList;

import ua.com.besqueet.mtwain.separpicker.data.Contact;

public class ContactsListChangedEvent {

    public ArrayList<Contact> contacts;

    public ContactsListChangedEvent(ArrayList<Contact> contacts){
        this.contacts = contacts;
    }

}
