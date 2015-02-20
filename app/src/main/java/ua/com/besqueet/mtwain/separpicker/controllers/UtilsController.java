package ua.com.besqueet.mtwain.separpicker.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import ua.com.besqueet.mtwain.separpicker.Constants;

public enum UtilsController implements Constants{
    INSTANCE;
    private boolean isTablet;

    public boolean isTablet() {
        return isTablet;
    }

    public void setTablet(boolean isTablet) {
        this.isTablet = isTablet;
    }

    public void saveData(String key,String jsonData){
        SharedPreferences sharedPreferences = ContextController.INSTANCE.getMainActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,jsonData);
        editor.commit();
    }

    public String loadData(String key){
        SharedPreferences sharedPreferences = ContextController.INSTANCE.getMainActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
}
