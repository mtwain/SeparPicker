package ua.com.besqueet.mtwain.separpicker.controllers;


import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.data.Contact;
import ua.com.besqueet.mtwain.separpicker.events.ContactsListChangedEvent;

public enum ContactsController implements Constants {

    INSTANCE;

    private ArrayList<Contact> contacts = new ArrayList<>();

    public void addContact(Contact contact){
        contacts.add(contact);
        Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
        String jsonContacts = new Gson().toJson(contacts, type);
        UtilsController.INSTANCE.saveData(CONTACTS,jsonContacts);
        BusController.INSTANCE.getBus().post(new ContactsListChangedEvent(contacts));
    }

    public void setContacts(ArrayList<Contact> newContacts){
        contacts = newContacts;
    }

    public ArrayList<Contact> loadContacts(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String jsonContacts = UtilsController.INSTANCE.loadData(CONTACTS);
                Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
                contacts = new Gson().fromJson(jsonContacts,type);
                return null;
            }
        }.execute();

        if(contacts==null){
            contacts = new ArrayList<Contact>();
            return new ArrayList<Contact>();
        }
        return contacts;
    }

    public ArrayList<Contact> getContacts(){
        return contacts;
    }

    public boolean isContains(String name){
        loadContacts();
        for (int i=0;i<contacts.size();i++){
            return contacts.get(i).call.equals(name);
        }
        return false;
    }

}
