package ua.com.besqueet.mtwain.separpicker.controllers;


import android.content.Context;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;

import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.Contract;
import ua.com.besqueet.mtwain.separpicker.data.Contact;
import ua.com.besqueet.mtwain.separpicker.events.ContactsListChangedEvent;

public enum ContactsController implements Constants {

    INSTANCE;
    private static final String L = "ContactsController";
    DB db = null;


    public void initDBforContacts(Context context){
        try {
            db = DBFactory.open(context, Contract.DB_NAME);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }


    public void addContact(Contact contact){
        try {
            ArrayList<Contact> loadedContacts = getContacts();
            loadedContacts.add(contact);
            db.put(Contract.KEY_CONTACTS, loadedContacts);
            Log.d(L,"Add contact final size: "+loadedContacts.size());
            BusController.INSTANCE.getBus().post(new ContactsListChangedEvent());
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Contact> getContacts(){
        try {
            ArrayList<Contact> retrievedContacts = (ArrayList<Contact>) db.getObject(Contract.KEY_CONTACTS, ArrayList.class);
            Log.d(L,"Get contacts: "+retrievedContacts.size());
            return retrievedContacts;
        } catch (SnappydbException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } catch (NullPointerException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public  Contact getContactById(Long id){
        ArrayList<Contact> loadedContacts = getContacts();
        for (int i=0;i<loadedContacts.size();i++){
            if(loadedContacts.get(i).id==id){
                return loadedContacts.get(i);
            }
        }
        return null;
    }

    public boolean isNameExists(String name){
        ArrayList<Contact> loadedContacts = getContacts();
        for (int i=0;i<loadedContacts.size();i++){
            if(loadedContacts.get(i).call.equals(name)){
                return true;
            }
        }
        return false;
    }

    public void deleteContact(Long id){
        ArrayList<Contact> loadedContact = getContacts();
        for (int i=0;i<loadedContact.size();i++){
            if (loadedContact.get(i).id==id){
                loadedContact.remove(i);
            }
        }
        try {
            db.put(Contract.KEY_CONTACTS, loadedContact);
        }
        catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void editContact(Long id,String call,String email,String number){
        ArrayList<Contact> loadedContact = getContacts();
        for (int i=0;i<loadedContact.size();i++){
            if (loadedContact.get(i).id==id){
                loadedContact.get(i);
                if(call!=null){
                    loadedContact.get(i).call = call;
                }
                if(email!=null){
                    loadedContact.get(i).email = email;
                }
                if(number!=null){
                    loadedContact.get(i).number = number;
                }
            }
        }
        try {
            db.put(Contract.KEY_CONTACTS, loadedContact);
        }
        catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}
