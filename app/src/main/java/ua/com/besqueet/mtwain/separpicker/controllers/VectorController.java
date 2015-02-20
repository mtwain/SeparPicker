package ua.com.besqueet.mtwain.separpicker.controllers;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.events.LoadVectorFinishEvent;

public enum VectorController {
    INSTANCE;

    public Drawable settings,users,shots,back,plus;

    public void loadVectors(final Context context){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    SVG settingsSVG = SVG.getFromResource(context, R.raw.settings);
                    SVG usersSVG = SVG.getFromResource(context, R.raw.users);
                    SVG shotsSVG = SVG.getFromResource(context, R.raw.list);
                    SVG backSVG = SVG.getFromResource(context, R.raw.back);
                    SVG plusSVG = SVG.getFromResource(context, R.raw.plus);
                    settings = new PictureDrawable(settingsSVG.renderToPicture());
                    users = new PictureDrawable(usersSVG.renderToPicture());
                    shots = new PictureDrawable(shotsSVG.renderToPicture());
                    back = new PictureDrawable(backSVG.renderToPicture());
                    plus = new PictureDrawable(plusSVG.renderToPicture());
                    BusController.INSTANCE.getBus().post(new LoadVectorFinishEvent());
                } catch (SVGParseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

}
