package ua.com.besqueet.mtwain.separpicker.ui.fragments.contacts;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContactsController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;
import ua.com.besqueet.mtwain.separpicker.data.Contact;
import ua.com.besqueet.mtwain.separpicker.events.ContactDeletedEvent;
import ua.com.besqueet.mtwain.separpicker.events.ContactItemClickEvent;
import ua.com.besqueet.mtwain.separpicker.events.ContactsListChangedEvent;

public class ContactsListFragment extends Fragment implements Constants{

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
                if(!UtilsController.INSTANCE.isTablet()) {
                    long id = contacts.get(i).id;
                    Bundle bundle = new Bundle();
                    bundle.putLong(BUNDLE_CONTACT_ID,id);
                    Fragment fragment = new ContactDetailFragment();

                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()

                            .add(R.id.containerContacts,fragment)
                            .addToBackStack("")
                            .commit();

                }else{
                    busInstance.getBus().post(new ContactItemClickEvent(contacts.get(i)));
                }
            }
        });

            contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Видалити контакт ?")
                            .setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .setPositiveButton("Так", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    contactsInstance.deleteContact(contacts.get(i).id);
                                    contacts.remove(i);
                                    contactsAdapter.notifyDataSetChanged();
                                    if(UtilsController.INSTANCE.isTablet()) {
                                        busInstance.getBus().post(new ContactDeletedEvent());
                                    }
                                }
                            });
                    builder.create().show();
                    return true;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                holder.textName = (TextView) vi.findViewById(R.id.textTime);
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
        contacts = contactsInstance.getContacts();
        contactsAdapter.notifyDataSetChanged();
    }

}
