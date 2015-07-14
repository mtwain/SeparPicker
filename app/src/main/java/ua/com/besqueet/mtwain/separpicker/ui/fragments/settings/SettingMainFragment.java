package ua.com.besqueet.mtwain.separpicker.ui.fragments.settings;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ua.com.besqueet.mtwain.separpicker.Constants;
import ua.com.besqueet.mtwain.separpicker.R;
import ua.com.besqueet.mtwain.separpicker.controllers.UtilsController;

public class SettingMainFragment extends Fragment implements Constants{

    public SettingMainFragment(){}

    @InjectView(R.id.textTime)
    TextView textCall;
    @InjectView(R.id.textCount)
    TextView textEmail;
    @InjectView(R.id.textPassword)
    TextView textPassword;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting_main,container,false);
        ButterKnife.inject(this, rootView);
        textCall.setText(UtilsController.INSTANCE.loadData(CALL));
        textPassword.setText(hideText(UtilsController.INSTANCE.loadData(PASSWORD)));
        textEmail.setText(UtilsController.INSTANCE.loadData(EMAIL));
        return rootView;
    }

    @OnClick(R.id.itemCall) void onCallItemClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext_view, null);
        final EditText editText = (EditText) view.findViewById(R.id.editName);
        editText.setText(UtilsController.INSTANCE.loadData(CALL));
        editText.setSelection(editText.getText().length(),editText.getText().length());
        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        UtilsController.INSTANCE.saveData(CALL,editText.getText().toString());
                        textCall.setText(editText.getText());
                    }
                })
                .setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create().show();
    }

    @OnClick(R.id.itemEmail) void onEmailItemClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext_view, null);
        final EditText editText = (EditText) view.findViewById(R.id.editName);
        editText.setText(UtilsController.INSTANCE.loadData(EMAIL));
        editText.setSelection(editText.getText().length(),editText.getText().length());
        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        UtilsController.INSTANCE.saveData(EMAIL, editText.getText().toString());
                        textEmail.setText(editText.getText());
                    }
                })
                .setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        UtilsController.INSTANCE.saveData(EMAIL, editText.getText().toString());
                        textEmail.setText(editText.getText());
                    }
                });
        builder.create().show();
    }

    @OnClick(R.id.itemPassword) void onPasswordItemClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext_password_view, null);
        final EditText editText = (EditText) view.findViewById(R.id.editName);
        editText.setText(UtilsController.INSTANCE.loadData(PASSWORD));
        editText.setSelection(editText.getText().length(),editText.getText().length());
        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        UtilsController.INSTANCE.saveData(PASSWORD, editText.getText().toString());
                        textPassword.setText(hideText(editText.getText().toString()));
                    }
                })
                .setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create().show();
    }

    @OnClick(R.id.btnBack) void onBackButtonClick(){
        getFragmentManager().popBackStack();
    }

    public String hideText(String string){
        String hidenString = "";
        for (int i = 0; i < string.length(); i++) {
            hidenString = hidenString + "*";
        }
        return hidenString;
    }

}

