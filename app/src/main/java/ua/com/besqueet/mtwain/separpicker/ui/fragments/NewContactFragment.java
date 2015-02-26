package ua.com.besqueet.mtwain.separpicker.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.ContactsController;
import ua.com.besqueet.mtwain.separpicker.data.Contact;

public class NewContactFragment extends Fragment{

    public NewContactFragment(){}

    @InjectView(R.id.editCall)
    EditText editCall;
    @InjectView(R.id.editEmail)
    EditText editEmail;
    @InjectView(R.id.editNumber)
    EditText editNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_contact,container,false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @OnClick(R.id.btnSave) void onSaveBtnClick(){
        if(!editCall.getText().equals("")&&!editEmail.getText().equals("")&&!editNumber.getText().equals("")){
                Contact contact = new Contact();
                contact.call = editCall.getText().toString();
                contact.email = editEmail.getText().toString();
                contact.number = editNumber.getText().toString();
                contact.id = System.currentTimeMillis();
                //додаємо контакт в бд
                if(!ContactsController.INSTANCE.isNameExists(contact.call)) {
                    ContactsController.INSTANCE.addContact(contact);
                    getFragmentManager().popBackStack();
                }
        }
    }

    @OnClick(R.id.btnCancel) void onCancelBtnClick(){
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.btnBack) void onBackBtnClick(){
        getFragmentManager().popBackStack();
    }

}
