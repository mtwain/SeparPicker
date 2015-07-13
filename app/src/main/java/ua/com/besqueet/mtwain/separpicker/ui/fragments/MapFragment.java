package ua.com.besqueet.mtwain.separpicker.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.FragmentAnimationDirection;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.BluetoothController;
import ua.com.besqueet.mtwain.separpicker.controllers.BusController;
import ua.com.besqueet.mtwain.separpicker.controllers.ContextController;
import ua.com.besqueet.mtwain.separpicker.controllers.ShotsController;
import ua.com.besqueet.mtwain.separpicker.data.BTR;
import ua.com.besqueet.mtwain.separpicker.data.BaseMarker;
import ua.com.besqueet.mtwain.separpicker.data.BasePoint;
import ua.com.besqueet.mtwain.separpicker.data.Cannon;
import ua.com.besqueet.mtwain.separpicker.data.MarkerType;
import ua.com.besqueet.mtwain.separpicker.data.Mortar;
import ua.com.besqueet.mtwain.separpicker.data.PointType;
import ua.com.besqueet.mtwain.separpicker.data.Tank;
import ua.com.besqueet.mtwain.separpicker.ui.CustomDialogClass;
import ua.com.besqueet.mtwain.separpicker.ui.fragments.contacts.ContactsFragment;
import ua.com.besqueet.mtwain.separpicker.ui.fragments.settings.SettingsFragment;
import ua.com.besqueet.mtwain.separpicker.ui.fragments.shots.ShotsFragment;

public class MapFragment extends Fragment implements Constants {

    @InjectView(R.id.btnSettings)
    ImageView btnSettings;
    @InjectView(R.id.btnContacts)
    ImageView btnContacts;
    @InjectView(R.id.btnShots)
    ImageView btnShots;
    @InjectView(R.id.tank)
    ImageView btnTank;
    @InjectView(R.id.BTR)
    ImageView btnBTR;
    @InjectView(R.id.cannon)
    ImageView btnCannon;
    @InjectView(R.id.mortar)
    ImageView btnMortar;
    //ImageView btnTank;

    public FrameLayout dialogButton;
    public EditText textAbout;
    public CustomDialogClass cdd;

    public MapFragment(){}

    BusController busInstance;

    MapView mapView;
    GoogleMap map;
    com.google.android.gms.maps.model.Marker marker;
    ArrayList<BaseMarker> markers = new ArrayList<>();
    ArrayList<Marker> gmarkers = new ArrayList<>();
    BasePoint myMapPoint;
    BaseMarker myMapMarker;
    CameraPosition cameraPosition;
    String icon = "";
    PointType type;
    String name = "", dialog_text_about = "";
    LayoutInflater inflater2;
    View layout;
    TextView text;
    String mailList = "";
    CountDownTimer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busInstance = BusController.INSTANCE;
        MapsInitializer.initialize(ContextController.INSTANCE.getMainActivity());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("aaa", "onDestroy");
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("aaa", "onStart");
        busInstance.getBus().register(this);
    }

    @Override
    public void onStop() {
        Log.d("aaa", "onStop");
        busInstance.getBus().unregister(this);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("aaa", "onSaveInstanceState");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("aaa", "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("aaa", "onDetach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("aaa", "onAC");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("aaa", "onAR");
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        Log.d("aaa", "onInflate");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("aaa", "onDV");
        cameraPosition = map.getCameraPosition();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map,container,false);
        ButterKnife.inject(this, rootView);
        mapView = (MapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        //btnTank = (ImageView) rootView.findViewById(R.id.tank);
        map = mapView.getMap();
        inflater2 = inflater;
        layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) container.findViewById(R.id.toast_layout_root));
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("aaa", "onViewCreated");
        if(markers.size() == 0){
          Log.d("aaa","markers is EMPTY");
        }else {
            showMarkers();
        }
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (icon.equals("")) {
                        showToast(0);
                    } else {
                        switch (type) {
                            case TANK:
                                myMapPoint = new Tank(name, latLng);
                                addMarker(latLng);
                                break;
                            case BTR:
                                myMapPoint = new BTR(name, latLng);
                                addMarker(latLng);
                                break;
                            case CANNON:
                                myMapPoint = new Cannon(name, latLng);
                                addMarker(latLng);
                                break;
                            case MORTAR:
                                myMapPoint = new Mortar(name, latLng);
                                addMarker(latLng);
                                break;
                        }
                    }
                }
            });
    }

    public void setShotName() {
        showDialogWindow("НАЗВА ШОТА", 1);
    }

    public void showToast(Integer check) {
        switch (check){
            case 0:
                text = (TextView) layout.findViewById(R.id.text);
                text.setText("Будь ласка, оберіть тип цілі!");
                break;
            case 1:
                text = (TextView) layout.findViewById(R.id.text);
                text.setText("Шот додано");
                break;
        }
        Toast toast = new Toast(ContextController.INSTANCE.getMainActivity());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void addMarker(LatLng latLng) {
        myMapPoint.setType(type);
        myMapMarker = new BasePoint(latLng);
        myMapMarker.setMarkerType(MarkerType.POINT);
        myMapMarker.setMarkerInfo(dialog_text_about);
        myMapMarker.setMarkerName(name);
        myMapMarker.setMarkerIcon(icon);
        double lat = latLng.latitude;
        double lon = latLng.longitude;
        myMapMarker.setMarkerPosition(lat,lon);
        Log.d("aaa", "LAT1=" + lat + "  LON1=" + lon);
        markers.add(myMapMarker);
        MarkerOptions user_options = new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title("Точка: " + myMapMarker.getMarkerName() + "   Опис: " + myMapMarker.getMarkerInfo())
                .icon(BitmapDescriptorFactory.fromResource(ShotsController.INSTANCE.manageMarkerIcon(icon)));
        marker = map.addMarker(user_options);
        gmarkers.add(marker);
        ShotsController.INSTANCE.setTemporaryList(markers);

    }

    public void showMarkers() {
        ArrayList<BaseMarker> markers = ShotsController.INSTANCE.getTemporaryList();
        for(int i=0;i<markers.size();i++){
            myMapMarker = markers.get(i);
                    String info = myMapMarker.getMarkerInfo();
                    Bundle bundle = myMapMarker.getMarkerPosition();
                    double lat = bundle.getDouble("LATITUDE");
                    double lon = bundle.getDouble("LONGITUDE");
                    LatLng position = new LatLng(lat,lon);
                    String name = myMapMarker.getMarkerName();
                    String icon = myMapMarker.getMarkerIcon();
                    MarkerOptions user_options = new MarkerOptions()
                            .position(position)
                            .title("Точка: " + name+"   Опис:" + info)
                            .icon(BitmapDescriptorFactory.fromResource(ShotsController.INSTANCE.manageMarkerIcon(icon)));
                    map.addMarker(user_options);
        }
        if(cameraPosition != null) {
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void showDialogWindow(String d_name, final Integer num) {
        String dialog_name = d_name;
        cdd = new CustomDialogClass(ContextController.INSTANCE.getMainActivity());
        cdd.setContentView(R.layout.custom_dialog);
        cdd.setTitle(dialog_name);
        cdd.show();
        dialogButton = (FrameLayout) cdd.findViewById(R.id.btn_save_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (num) {
                    case 0:
                        textAbout = (EditText) cdd.findViewById(R.id.dialog_text_about);
                        dialog_text_about = textAbout.getText().toString();
                        textAbout.setText("");
                        Log.d("aaa", "1--->" + dialog_text_about);
                        break;
                    case 1:
                        textAbout = (EditText) cdd.findViewById(R.id.dialog_text_about);
                        dialog_text_about = textAbout.getText().toString();
                        textAbout.setText("");
                        ShotsController.INSTANCE.addShot(dialog_text_about,cameraPosition);
                        showToast(1);
                        Log.d("aaa", "2--->" + dialog_text_about);
                        break;
                }
                cdd.dismiss();
            }
        });
    }

    @OnClick(R.id.btnSettings) void onSettingsBtnClick(){
        ContextController.INSTANCE.getMainActivity().presentFragmentAbove(new SettingsFragment(), FragmentAnimationDirection.FROM_LEFT);
    }
    @OnClick(R.id.btnShots) void onShotsBtnClick(){
        ContextController.INSTANCE.getMainActivity().presentFragmentAbove(new ShotsFragment(), FragmentAnimationDirection.FROM_LEFT);
    }
    @OnClick(R.id.btnContacts) void onContactsBtnClick(){
        ContextController.INSTANCE.getMainActivity().presentFragmentAbove(new ContactsFragment(), FragmentAnimationDirection.FROM_LEFT);
    }

    //TODO ?
    /*public void onTankClick() {
        showDialogWindow("ОПИС ТОЧКИ", 0);
        icon = "tank";
        name = "Tank";
        type = PointType.TANK;
    }*/
    @OnClick(R.id.tank) void onTankClick(){
        showDialogWindow("ОПИС ТОЧКИ", 0);
        icon = "tank";
        name = "Tank";
        type = PointType.TANK;
    }
    @OnClick(R.id.BTR) void onBTRClick(){
        showDialogWindow("ОПИС ТОЧКИ", 0);
        icon = "BTR";
        name = "BTR";
        type = PointType.BTR;
    }
    @OnClick(R.id.cannon) void onCannonClick(){
        showDialogWindow("ОПИС ТОЧКИ", 0);
        icon = "cannon";
        name = "Cannon";
        type = PointType.CANNON;
    }
    @OnClick(R.id.mortar) void onMortarClick(){
        showDialogWindow("ОПИС ТОЧКИ", 0);
        icon = "mortar";
        name = "Mortar";
        type = PointType.MORTAR;
    }
    @OnClick(R.id.saveShot) void saveMarker(){
        showDialogWindow("НАЗВА ШОТА",1);
    }
    @OnClick(R.id.getMail) void getMailMarkers() {
        timer = new CountDownTimer(20000, 20) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try {
                    Thread thread = new Thread(new Runnable() {
                        String from = "";
                        String to = "";
                        String subject = "";
                        String text = "";

                        @Override
                        public void run() {
                            Properties props = System.getProperties();
                            props.setProperty("mail.store.protocol", "imaps");
                            Log.d("aaa", "1");
                            try {
                                Session session = Session.getDefaultInstance(props, null);
                                Store store = session.getStore("imaps");
                                Log.d("aaa", "2");
                                store.connect("imap.gmail.com", "solomon26061994@gmail.com", "Maxym_Tarasyuk");
                                Log.d("aaa", "3");
                                Folder inbox = store.getFolder("INBOX");
                                Log.d("aaa", "4");
                                inbox.open(inbox.READ_WRITE);
                                Log.d("aaa", "5");
                                FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
                                Message result[] = inbox.search(ft);
                                Log.d("aaa", "6");
                                for (int i = 0, n = result.length; i < n; i++) {
                                    Message message = result[i];
                                    from = InternetAddress.toString(message.getFrom());
                                    to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
                                    subject = message.getSubject();
                                    text = message.getContent().toString();
                                    mailList = text;
                                    Log.d("aaa", "Subject: " + subject);
                                    Log.d("aaa", "From: " + from);
                                    Log.d("aaa", "To: " + to);
                                    Log.d("aaa", "Text: " + text);
                                    if (!mailList.equals("")) {
                                        ArrayList<BaseMarker> mailListArray = ShotsController.INSTANCE.deserialize(mailList);
                                        ShotsController.INSTANCE.addShot2(subject, mailListArray);
                                    } else {
                                        Log.d("aaa", "EMPTY MAILLIST");
                                    }
                                    message.setFlag(Flags.Flag.SEEN, true);
                                }
                                Log.d("aaa", "STORE CLOSE");
                                inbox.close(true);
                                store.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    timer.start();
                } catch (Exception e) {
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
    }
}
