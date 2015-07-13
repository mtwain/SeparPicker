package ua.com.besqueet.mtwain.separpicker.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import ua.com.besqueet.mtwain.separpicker.R;

/**
 * Created by maxym_tarasyuk on 4/16/15.
 */
public class CustomDialogClass extends Dialog {

    public Activity c;
    public Dialog d;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

    }
}
