package ua.com.besqueet.mtwain.separpicker.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.data.BaseMarker;

/**
 * Created by maxym_tarasyuk on 6/13/15.
 */
public enum BluetoothController implements Constants{
    INSTANCE;

    String deviceName = "";
    Integer check = 0;

    public void setCheck(Integer ch){this.check = ch;}

    public BluetoothSPP bt;
     public void setBluetooth() {
         bt = new BluetoothSPP(ContextController.INSTANCE.getMainActivity());
     }
    public void btConnect(Intent data) {
        bt.connect(data);
    }
    public void setupBlService() {
        bt.setupService();
        bt.startService(BluetoothState.DEVICE_ANDROID);
    }
    public int checkBluetoothEnable() {
        Integer i = 0;
        if(!bt.isBluetoothEnabled()) {
            i = 1;
        }
        return i;
    }
    public Integer checkServiceAvailable() {
        Integer i = 0;
        if(!bt.isServiceAvailable()) {
            i = 1;
        }
        return i;
    }
    public Integer checkBluetoothAvailable() {
        Integer i = 0;
        if(!bt.isBluetoothAvailable()) {
            i = 1;
        }
        return i;
    }
    public void setOnDataReceivedListener() {
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(ContextController.INSTANCE.getMainActivity(), "You have a new BlutShot", Toast.LENGTH_SHORT).show();
                ArrayList<BaseMarker> blutListArray = ShotsController.INSTANCE.deserialize(message);
                ShotsController.INSTANCE.addShot2("BlutShot", blutListArray);
            }//TODO перенаправити шот у відповідні функції
        });
    }

    public void setBluetoothConnectionListener(final ArrayList<BaseMarker> points){
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                if(check == 1) {
                    Toast.makeText(ContextController.INSTANCE.getMainActivity()
                            , "Connected to " + name + "\n" + address
                            , Toast.LENGTH_SHORT).show();
                    Log.d("aaa", "AAAAAAA0");
                    send(points);
                    deviceName = name;
                }else{
                    Toast.makeText(ContextController.INSTANCE.getMainActivity()
                            , "You can't send shot"
                            , Toast.LENGTH_SHORT).show();
                }
            }

            public void onDeviceDisconnected() {
                Toast.makeText(ContextController.INSTANCE.getMainActivity()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(ContextController.INSTANCE.getMainActivity()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void send(ArrayList<BaseMarker> points){
        ArrayList<BaseMarker> shotList = points;
        SharedPreferences sharedPreferences = ShotsController.INSTANCE.serialize(shotList);
        String listString = sharedPreferences.getString(MARKER_LIST, "");
        Log.d("aaa","SENDSENDSEND "+listString);
            bt.send("" + listString, true);
    }
}
