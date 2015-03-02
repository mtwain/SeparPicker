package ua.com.besqueet.mtwain.separpicker.controllers;


import android.content.Context;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;

import ua.com.besqueet.mtwain.separpicker.Contract;
import ua.com.besqueet.mtwain.separpicker.data.BasePoint;
import ua.com.besqueet.mtwain.separpicker.data.Shot;
import ua.com.besqueet.mtwain.separpicker.events.ShotsListChangedEvent;

public enum  ShotsController {
    INSTANCE;

    DB db = null;
    private static final String L = "ShotsController";

    public void initDBforShot(Context context){
        try {
            db = DBFactory.open(context, Contract.DB_NAME);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void addShot(Shot shot){
        try {
            ArrayList<Shot> loadedShots = getShots();
            loadedShots.add(shot);
            db.put(Contract.KEY_SHOTS, loadedShots);
            Log.d(L, "Add shot final size: " + loadedShots.size());
            BusController.INSTANCE.getBus().post(new ShotsListChangedEvent());
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Shot> getShots(){
        try {
            ArrayList<Shot> retrievedShots = (ArrayList<Shot>) db.getObject(Contract.KEY_SHOTS, ArrayList.class);
            Log.d(L,"Get shots: "+retrievedShots.size());
            return retrievedShots;
        } catch (SnappydbException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }catch (NullPointerException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Shot getShotById(Long id){
        ArrayList<Shot> loadedShots = getShots();
        for (int i=0;i<loadedShots.size();i++){
            if(loadedShots.get(i).id.equals(id)){
                return loadedShots.get(i);
            }
        }
        return null;
    }

    public boolean isNameExists(String name){
        ArrayList<Shot> loadedShots = getShots();
        for (int i=0;i<loadedShots.size();i++){
            if(loadedShots.get(i).name.equals(name)){
                return true;
            }
        }
        return false;
    }

    public void deleteShot(Long id){
        ArrayList<Shot> loadedShots = getShots();
        for (int i=0;i<loadedShots.size();i++){
            if (loadedShots.get(i).id.equals(id)){
                loadedShots.remove(i);
            }
        }
        try {
            db.put(Contract.KEY_SHOTS, loadedShots);
        }
        catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void editShot(Long id,String name,Long time,ArrayList<BasePoint> points){
        ArrayList<Shot> loadedShots = getShots();
        for (int i=0;i<loadedShots.size();i++){
            Log.d("L","Loaded: "+loadedShots.get(i).id+ "| my: "+id);
            if (loadedShots.get(i).id.equals(id)){

                loadedShots.get(i);

                if(name!=null){
                    loadedShots.get(i).name = name;
                }
                if(points!=null){
                    loadedShots.get(i).points = points;
                    loadedShots.get(i).time = time;
                }

            }
        }
        try {
            db.put(Contract.KEY_SHOTS, loadedShots);
            BusController.INSTANCE.getBus().post(new ShotsListChangedEvent());
        }
        catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

}
