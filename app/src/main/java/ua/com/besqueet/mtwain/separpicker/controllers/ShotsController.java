package ua.com.besqueet.mtwain.separpicker.controllers;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.Calendar;

import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.Contract;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.data.BaseMarker;
import ua.com.besqueet.mtwain.separpicker.data.OutShot;
import ua.com.besqueet.mtwain.separpicker.data.Shot;
import ua.com.besqueet.mtwain.separpicker.data.ShotType;
import ua.com.besqueet.mtwain.separpicker.events.ShotsListChangedEvent;

public enum  ShotsController {
    INSTANCE;

    DB db = null;
    private static final String L = "ShotsController";
    ArrayList<BaseMarker> temporaryList;
    String list;

    public DB getDB(){return this.db;}

    public void initDBforShot(Context context){
        try {
            db = DBFactory.open(context, Contract.DB_NAME);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public SharedPreferences serialize(ArrayList<BaseMarker> points) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(points);
        SharedPreferences sharedPreferences = ContextController.INSTANCE.getMainActivity().getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("MARKER_LIST", jsonString).apply();
        return sharedPreferences;
    }

    public ArrayList<BaseMarker> deserialize(String list) {

        ArrayList<BaseMarker> markerList = new ArrayList<>();
        if(!list.equals("")){
            Gson gson1 = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(list).getAsJsonArray();
            for(int i=0; i<array.size(); i++){
                BaseMarker bm = gson1.fromJson(array.get(i), BaseMarker.class);
                markerList.add(bm);
            }
        }
        return markerList;
    }

    public void setTemporaryList(ArrayList<BaseMarker> temporaryList) {
        this.temporaryList = new ArrayList<>();
        this.temporaryList = temporaryList;
        Log.d("aaa","SET="+this.temporaryList.size());
    }

    public int manageMarkerIcon(String markerIcon)
    {
        switch (markerIcon){
            case "tank":
                return R.drawable.tank;
            case "BTR":
                return R.drawable.btr;
            case "cannon":
                return R.drawable.cannon;
            case "mortar":
                return R.drawable.mortar;
        }
        return R.drawable.ic_launcher;
    }

    public ArrayList<BaseMarker> getTemporaryList() {

        Log.d("aaa","GET="+this.temporaryList.size());
        return this.temporaryList;
    }

    public void loadSharedPrefs(SharedPreferences sharedPreferences) {
        this.list = sharedPreferences.getString(Constants.MARKER_LIST, "");
    }

    public void saveShotFromMap(String name){
        /*String jsonList = list.getString("MARKER_LIST", "");
        Log.d("aaa","ADD="+jsonList);
        if(!jsonList.equals("")){
            Gson gson1 = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(jsonList).getAsJsonArray();
            markerList = new ArrayList<>();
            for(int i=0; i<array.size(); i++){
                BaseMarker bm = gson1.fromJson(array.get(i), BaseMarker.class);
                markerList.add(bm);
                Log.d("aaa",""+array.size());
            }
        }*/
        //markerList = mapFragment.deserialize(jsonList);

        OutShot shot = new OutShot();
        Long id = System.currentTimeMillis();

        String date = getCurrentDate();

        shot.time = date;
        shot.id = id;
        shot.type = ShotType.OUT;
        shot.points = temporaryList;
        shot.name = name;
        shot.isRead = true;

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

    public void addShot2(String name, ArrayList<BaseMarker> list){
        OutShot shot = new OutShot();
        Long id = System.currentTimeMillis();

        String date = getCurrentDate();

        shot.time = date;
        shot.id = id;
        shot.type = ShotType.OUT;
        shot.points = list;
        shot.name = name;

        try {
            Log.d(L,"saveShotFromMap"+shot.points.size());
            ArrayList<Shot> loadedShots = getShots();
            loadedShots.add(shot);
            db.put(Contract.KEY_SHOTS, loadedShots);
            Log.d(L, "Add shot final size: " + loadedShots.size());
            BusController.INSTANCE.getBus().post(new ShotsListChangedEvent());
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        String date = day+"."+month+"."+year;
        return date;
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


    public void readShot(Long id) {
        ArrayList<Shot> loadedShots = getShots();
        for (int i = 0; i < loadedShots.size(); i++) {
            if (loadedShots.get(i).id.equals(id)) {
                loadedShots.get(i).isRead = true;
            }
        }
        try {
            db.put(Contract.KEY_SHOTS, loadedShots);
            BusController.INSTANCE.getBus().post(new ShotsListChangedEvent());
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void editShot(Long id,String name,String time,ArrayList<BaseMarker> points){
        ArrayList<Shot> loadedShots = getShots();
        for (int i=0;i<loadedShots.size();i++){
            Log.d("L","Loaded: "+loadedShots.get(i).id+ "| my: "+id);
            if (loadedShots.get(i).id.equals(id)){

                loadedShots.get(i);

                if(name!=null){
                    loadedShots.get(i).name = name;
                    loadedShots.get(i).time = time;
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
