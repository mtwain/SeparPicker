package ua.com.besqueet.mtwain.separpicker.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.halfbit.tinybus.Subscribe;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.events.ContactItemClickEvent;

public class ContactDetailFragment extends Fragment {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busInstance = BusController.INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_detail,container,false);
        ButterKnife.inject(this, rootView);
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

    @Subscribe
    public void onContactItemCLick(ContactItemClickEvent event){
        if(event.contact!=null) {
            rootLayout.setVisibility(View.VISIBLE);
            textCall.setText(event.contact.call);
            textEmail.setText(event.contact.email);
            textNumber.setText(event.contact.number);
        }else {
            rootLayout.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.editCall) void onEditCallClick(){

    }

    @OnClick(R.id.editEmail) void onEditEmailClick(){

    }

    @OnClick(R.id.editNumber) void onEditNumberClick(){

    }

}
