package ua.com.besqueet.mtwain.separpicker.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.ToolBarKeeper;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;
import ua.com.besqueet.mtwain.separpicker.controllers.VectorController;

public class ContactsFragment extends Fragment implements ToolBarKeeper{

    public ContactsFragment(){}

    @InjectView(R.id.btnBack)
    ImageView btnBack;
    @InjectView(R.id.btnAdd)
    ImageView btnAdd;
    @InjectView(R.id.barTitle)
    TextView barTitle;
    VectorController vectorInstance;
    BusController busInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vectorInstance = VectorController.INSTANCE;
        busInstance = BusController.INSTANCE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts,container,false);
        ButterKnife.inject(this, rootView);
        createView();
        getFragmentManager().beginTransaction()
                .add(R.id.containerContacts, new ContactsListFragment())
                .commit();
        if(UtilsController.INSTANCE.isTablet()){
        getFragmentManager().beginTransaction()
                 .add(R.id.containerDetail, new ContactDetailFragment())
                 .commit();
        }
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(!UtilsController.INSTANCE.isTablet()&&getFragmentManager()!=null){
                    int count = getFragmentManager().getBackStackEntryCount();
                    Log.d("L","Count: "+count);
                    if(count==2){
                        barTitle.setText("Контакт");
                    }else if(count==1){
                        barTitle.setText("Контакти");
                    }
                }
            }
        });
        return rootView;
    }


    @OnClick(R.id.btnAdd) void onAddBtnClick(){
        ContextController.INSTANCE.getMainActivity().presentFragmentAbove(new NewContactFragment());
    }

    @OnClick(R.id.btnBack) void onBackBtnClick(){
        getActivity().getFragmentManager().popBackStack();
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

    @Override
    public void createView() {
        btnBack.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        btnAdd.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        btnBack.setImageDrawable(vectorInstance.back);
        btnAdd.setImageDrawable(vectorInstance.plus);
    }


}
