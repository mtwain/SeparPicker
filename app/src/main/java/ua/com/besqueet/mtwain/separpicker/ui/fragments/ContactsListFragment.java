package ua.com.besqueet.mtwain.separpicker.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.halfbit.tinybus.Subscribe;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContactsController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.data.Contact;
import ua.com.besqueet.mtwain.separpicker.events.ContactItemClickEvent;
import ua.com.besqueet.mtwain.separpicker.events.ContactsListChangedEvent;

public class ContactsListFragment extends Fragment{

    public ContactsListFragment(){}

    @InjectView(R.id.contactList)
    ListView contactList;
    ContactsAdapter contactsAdapter;
    ContactsController contactsInstance;
    BusController busInstance;

    ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsAdapter = new ContactsAdapter();
        contactsInstance = ContactsController.INSTANCE;
        busInstance = BusController.INSTANCE;
        contacts = contactsInstance.getContacts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts_list,container,false);
        ButterKnife.inject(this, rootView);
        contactList.setAdapter(contactsAdapter);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                busInstance.getBus().post(new ContactItemClickEvent(contacts.get(i)));
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!busInstance.getBus().hasRegistered(this))
            busInstance.getBus().register(this);
    }

    @Override
    public void onStop() {
        busInstance.getBus().unregister(this);
        super.onStop();
    }

    class ContactsAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public Object getItem(int i) {
            return contacts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = ContextController.INSTANCE.getMainActivity().getLayoutInflater();
            View vi = view;
            ViewHolder holder;
            if (vi == null) {
                vi = inflater.inflate(R.layout.cell_contact, viewGroup, false);
                holder = new ViewHolder();
                holder.textName = (TextView) vi.findViewById(R.id.textName);
                vi.setTag(holder);
            }else {
                holder = (ViewHolder) vi.getTag();
            }
            holder.textName.setText(contacts.get(i).call);
            return vi;
        }
    }

    static class ViewHolder {
        TextView textName;
    }

    @Subscribe
    public void onContactsListChangedListener(ContactsListChangedEvent event){
        contacts = event.contacts;
        contactsAdapter.notifyDataSetChanged();
    }

}
