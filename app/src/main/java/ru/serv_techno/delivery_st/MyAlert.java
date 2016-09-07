package ru.serv_techno.delivery_st;

/**
 * Created by Maxim on 08.09.2016.
 */

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;


/**
 * Created by Maxim on 08.09.2016.
 */
public class MyAlert extends DialogFragment {
    public static MyAlert newInstance(String title, String message, int DialogID) {
        MyAlert frag = new MyAlert();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putInt("DialogID", DialogID);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments(); //Получаем заголовок и сообщение
        String title = args.getString("title");
        String message = args.getString("message");
        final int DialogID = args.getInt("DialogID");

        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int whichButton) {

                                if(DialogID==1|DialogID==2){
                                    ((BoxActivity) getActivity()).doPositiveClick();
                                }

                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int whichButton) {
                                dialog.cancel();
                            }
                        })
                .create();
    }
}

