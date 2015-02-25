package ua.com.besqueet.mtwain.separpicker.ui.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.halfbit.tinybus.Subscribe;
import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContactsController;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;
import ua.com.besqueet.mtwain.separpicker.data.Contact;
import ua.com.besqueet.mtwain.separpicker.events.ContactDeletedEvent;
import ua.com.besqueet.mtwain.separpicker.events.ContactItemClickEvent;

public class ContactDetailFragment extends Fragment implements Constants {

    public ContactDetailFragment(){}
    BusController busInstance;

    @InjectView(R.id.root_layout)
    LinearLayout rootLayout;
    @InjectView(R.id.textCall)
    TextView textCall;
    @InjectView(R.id.textEmail)
    TextView textEmail;
    @InjectView(R.id.textNumber)
    TextView textNumber;
    Long idContact;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busInstance = BusController.INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_detail,container,false);
        ButterKnife.inject(this, rootView);
        if(!UtilsController.INSTANCE.isTablet()) {
            Long id = getArguments().getLong(BUNDLE_CONTACT_ID);
            Contact contact = ContactsController.INSTANCE.getContactById(id);
            id = contact.id;
            if(contact!=null){
                setValues(contact.call,contact.email,contact.number);
            }else {
                getFragmentManager().popBackStack();
                Log.d("L","Контакту не існує");
            }
        }
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        busInstance.getBus().register(this);
    }

    @Override
    public void onStop() {
        busInstance.getBus().unregister(this);
        super.onStop();
    }

    public void setValues(String call,String email,String number){
        textCall.setText(call);
        textEmail.setText(email);
        textNumber.setText(number);
    }

    @Subscribe
    public void onContactItemCLick(ContactItemClickEvent event){
        if(event.contact!=null) {
            rootLayout.setVisibility(View.VISIBLE);
            textCall.setText(event.contact.call);
            textEmail.setText(event.contact.email);
            textNumber.setText(event.contact.number);
            idContact = event.contact.id;
        }else {
            rootLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe
    public void onContactDeleted(ContactDeletedEvent event){
        textCall.setText("");
        textEmail.setText("");
        textNumber.setText("");
        rootLayout.setVisibility(View.INVISIBLE);
    }


    @OnClick(R.id.editCall) void onEditCallClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext_view, null);
        TextView textView = (TextView) view.findViewById(R.id.textTitle);
        textView.setText("Позивний");
        final EditText editText = (EditText) view.findViewById(R.id.editName);
        editText.setText(textCall.getText());
        editText.setSelection(editText.getText().length(),editText.getText().length());
        builder.setView(view)
                .setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        textCall.setText(editText.getText());
                        ContactsController.INSTANCE.editContact(idContact,editText.getText().toString(),null,null);
                    }
                });
        builder.create().show();
    }

    @OnClick(R.id.editEmail) void onEditEmailClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext_view, null);
        TextView textView = (TextView) view.findViewById(R.id.textTitle);
        textView.setText("Email адреса");
        final EditText editText = (EditText) view.findViewById(R.id.editName);
        editText.setText(textEmail.getText());
        editText.setSelection(editText.getText().length(),editText.getText().length());
        builder.setView(view)
                .setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        textEmail.setText(editText.getText());
                        ContactsController.INSTANCE.editContact(idContact,null,editText.getText().toString(),null);
                    }
                });
        builder.create().show();
    }

    @OnClick(R.id.editNumber) void onEditNumberClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext_view, null);
        TextView textView = (TextView) view.findViewById(R.id.textTitle);
        textView.setText("Номер телефону");
        final EditText editText = (EditText) view.findViewById(R.id.editName);
        editText.setText(textNumber.getText());
        editText.setSelection(editText.getText().length(),editText.getText().length());
        builder.setView(view)
                .setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ContactsController.INSTANCE.editContact(idContact,null,null,editText.getText().toString());
                        textNumber.setText(editText.getText());
                    }
                });
        builder.create().show();
    }

}
