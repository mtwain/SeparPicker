package ua.com.besqueet.mtwain.separpicker.ui.fragments.shots;


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
import ua.com.besqueet.mtwain.separpicker.controllers.ShotsController;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;
import ua.com.besqueet.mtwain.separpicker.data.OutShot;
import ua.com.besqueet.mtwain.separpicker.data.Shot;
import ua.com.besqueet.mtwain.separpicker.data.ShotType;
import ua.com.besqueet.mtwain.separpicker.events.ShotDeletedEvent;
import ua.com.besqueet.mtwain.separpicker.events.ShotItemClickEvent;

public class ShotDetailFragment extends Fragment implements Constants {

    public ShotDetailFragment(){}
    BusController busInstance;

    @InjectView(R.id.root_layout)
    LinearLayout rootLayout;
    @InjectView(R.id.textTime)
    TextView textTime;
    @InjectView(R.id.textName)
    TextView textName;
    @InjectView(R.id.textCount)
    TextView textCount;
    @InjectView(R.id.textIsSended)
    TextView textSended;


    Long idShot;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busInstance = BusController.INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shot_detail,container,false);
        ButterKnife.inject(this, rootView);
        if(!UtilsController.INSTANCE.isTablet()) {
            Long id = getArguments().getLong(BUNDLE_SHOT_ID);
            Shot shot = ShotsController.INSTANCE.getShotById(id);
            idShot = shot.id;
            if(shot!=null){
                if(shot.type.equals(ShotType.OUT)){
                    OutShot outShot = (OutShot) shot;
                    setValues(shot.type,shot.name,outShot.isSended,shot.points.size(),shot.time);
                }else {
                    setValues(shot.type,shot.name,false,shot.points.size(),shot.time);
                }
            }else {
                getFragmentManager().popBackStack();
                Log.d("L","Шоту не існує");
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

    public void setValues(ShotType shotType,String name,boolean status,int count,long time){
        if(shotType.equals(ShotType.IN)){
            textSended.setText("Отриманий");
        }else {
            if(status){
                textSended.setText("Відправлений");
            }else {
                textSended.setText("Не відправлений");
            }
        }
        textName.setText(name);
        textTime.setText(""+time);
        textCount.setText(count+"");
    }

    @Subscribe
    public void onContactItemCLick(ShotItemClickEvent event){
        if(event.shot!=null) {
            idShot = event.shot.id;
            Log.d("L","Event: "+textCount+" | "+textTime+" | "+textSended);
            rootLayout.setVisibility(View.VISIBLE);
            textName.setText(event.shot.name);
            textCount.setText(event.shot.points.size()+"");
            textTime.setText(""+event.shot.time);

            if(event.shot.type.equals(ShotType.IN)){
                textSended.setText("Отриманий");
            }else {
                OutShot outShot = (OutShot) event.shot;
                if(outShot.isSended){
                    textSended.setText("Відправлений");
                }else {
                    textSended.setText("Не відправлений");
                }
            }
        }else {
            rootLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe
    public void onShotDeleted(ShotDeletedEvent event){
        textCount.setText("");
        textName.setText("");
        textSended.setText("");
        textTime.setText("");
        rootLayout.setVisibility(View.INVISIBLE);
    }


    @OnClick(R.id.editName) void onEditCallClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext_view, null);
        TextView textView = (TextView) view.findViewById(R.id.textTitle);
        textView.setText("Назва");
        final EditText editText = (EditText) view.findViewById(R.id.editName);
        editText.setText(textName.getText());
        editText.setSelection(editText.getText().length(),editText.getText().length());
        builder.setView(view)
                .setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("L","Id:"+id);
                        textName.setText(editText.getText());
                        ShotsController.INSTANCE.editShot(idShot,editText.getText().toString(),System.currentTimeMillis(),null);
                    }
                });
        builder.create().show();
    }

}
