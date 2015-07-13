package ua.com.besqueet.mtwain.separpicker.ui.fragments.shots;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.akexorcist.bluetotohspp.library.BluetoothState;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.halfbit.tinybus.Subscribe;
import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.bluetooth.DeviceList;
import ua.com.besqueet.mtwain.separpicker.controllers.BluetoothController;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.controllers.ShotsController;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;
import ua.com.besqueet.mtwain.separpicker.data.BaseMarker;
import ua.com.besqueet.mtwain.separpicker.data.OutShot;
import ua.com.besqueet.mtwain.separpicker.data.Shot;
import ua.com.besqueet.mtwain.separpicker.data.ShotType;
import ua.com.besqueet.mtwain.separpicker.events.ShotDeletedEvent;
import ua.com.besqueet.mtwain.separpicker.events.ShotItemClickEvent;
import ua.com.besqueet.mtwain.separpicker.ui.fragments.MapReviewFragment;
import ua.com.besqueet.mtwain.separpicker.ui.fragments.contacts.ContactsListFragment2;

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
    @InjectView(R.id.btnBLDevice)
    TextView btnBLDevice;

    Long idShot;
    public ArrayList<BaseMarker> markers;
    Shot shot;
    String shotName = "";
    ArrayList<BaseMarker> tabletShotPoints;
    String tabletShotName = "";
    Integer checkBL = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busInstance = BusController.INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shot_detail,container,false);
        ButterKnife.inject(this, rootView);
        //BluetoothController.INSTANCE.setBluetooth();

        if(!UtilsController.INSTANCE.isTablet()) {
            Long id = getArguments().getLong(BUNDLE_SHOT_ID);
            shot = ShotsController.INSTANCE.getShotById(id);
            idShot = shot.id;
            markers = new ArrayList<BaseMarker>();
            markers = shot.points;
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

    public void onDestroy() {
        super.onDestroy();
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

    public void setValues(ShotType shotType,String name,boolean status,int count,String time){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                BluetoothController.INSTANCE.btConnect(data);//bt.connect(data);
        }else{
            if(resultCode == Activity.RESULT_OK)
                starDeviceActivity();

        }
    }

    @Subscribe
    public void onContactItemCLick(ShotItemClickEvent event){
        if(event.shot!=null) {
            tabletShotPoints = new ArrayList<>();
            idShot = event.shot.id;
            Log.d("L", "Event: " + textCount + " | " + textTime + " | " + textSended);
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
            tabletShotPoints = event.shot.points;
            tabletShotName = event.shot.name;
        }else {
            rootLayout.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.btnShow) void onBtnEditClick(){
        if(!UtilsController.INSTANCE.isTablet()) {
            ArrayList<BaseMarker> shotList = shot.points;
            setMapReviewFragment(shotList);
        }//TODO додав перевірку на планшет
        else{
            ArrayList<BaseMarker> shotList = tabletShotPoints;
            setMapReviewFragment(shotList);
        }
    }

    public void setMapReviewFragment(ArrayList<BaseMarker> shotList){
        SharedPreferences sharedPreferences = ShotsController.INSTANCE.serialize(shotList);
        Fragment fragment = new MapReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MARKER_LIST, sharedPreferences.getString(MARKER_LIST, ""));
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.containerContacts, fragment)
                .addToBackStack("")
                .commit();
    }

    @Subscribe
    public void onShotDeleted(ShotDeletedEvent event){
        textCount.setText("");
        textName.setText("");
        textSended.setText("");
        textTime.setText("");
        rootLayout.setVisibility(View.INVISIBLE);
    }



    @OnClick(R.id.btnSend) void onBtnSendClick(){
        if(!UtilsController.INSTANCE.isTablet()) {
            ArrayList<BaseMarker> shotList = shot.points;
            SharedPreferences sharedPreferences = ShotsController.INSTANCE.serialize(shotList);
            String listString = sharedPreferences.getString(MARKER_LIST, "");
            Bundle bundle = new Bundle();
            bundle.putString(MARKER_LIST, listString);
            shotName = shot.name;
            bundle.putString(MARKER_NAME, shotName);
            Fragment fragment = new ContactsListFragment2();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.containerContacts, fragment)
                    .addToBackStack("")
                    .commit();
        }else{
            ArrayList<BaseMarker> shotList = tabletShotPoints;
            SharedPreferences sharedPreferences = ShotsController.INSTANCE.serialize(shotList);
            String listString = sharedPreferences.getString(MARKER_LIST, "");
            Bundle bundle = new Bundle();
            bundle.putString(MARKER_LIST, listString);
            shotName = tabletShotName;
            bundle.putString(MARKER_NAME, shotName);
            Fragment fragment = new ContactsListFragment2();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.containerContacts, fragment)
                    .addToBackStack("")
                    .commit();
        }
    }

    @OnClick(R.id.btnBLDevice) void onBtnChooseDeviceClick(){//TODO Прибрав це, для того, щоб не просило ввімкнути блютуз, коли вертаюсь назад до деталей шота з актівіті з лістом девайсів
        if(BluetoothController.INSTANCE.checkBluetoothAvailable() == 1) {
            Toast.makeText(ContextController.INSTANCE.getMainActivity()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
        }



        BluetoothController.INSTANCE.setOnDataReceivedListener();

        if(!UtilsController.INSTANCE.isTablet()){
        BluetoothController.INSTANCE.setBluetoothConnectionListener(shot.points);}
        else{
            BluetoothController.INSTANCE.setBluetoothConnectionListener(tabletShotPoints);
        }

        if (BluetoothController.INSTANCE.checkBluetoothEnable() == 1) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
            Log.d("aaa","CSA");
        } else {
            starDeviceActivity();
            if(BluetoothController.INSTANCE.checkServiceAvailable() == 1) {
                Log.d("aaa", "CSA--SBLS");
                BluetoothController.INSTANCE.setupBlService();
            }
        }

        BluetoothController.INSTANCE.setOnDataReceivedListener();
        /*Intent intent = null;
        intent = new Intent(ContextController.INSTANCE.getMainActivity(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);*/
    }


    @OnClick(R.id.btnSendBL) void sendBLMessage(){
        if(!UtilsController.INSTANCE.isTablet()){
            BluetoothController.INSTANCE.send(shot.points);
        }else{
            BluetoothController.INSTANCE.send(tabletShotPoints);
        }

    }

    public void starDeviceActivity(){
        Intent intent = null;
        intent = new Intent(ContextController.INSTANCE.getMainActivity(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
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
                        String date = ShotsController.INSTANCE.getCurrentDate();
                        Log.d("aaa","DATE="+date);
                        ShotsController.INSTANCE.editShot(idShot,editText.getText().toString(),date,null);
                    }
                });
        builder.create().show();
    }

}
